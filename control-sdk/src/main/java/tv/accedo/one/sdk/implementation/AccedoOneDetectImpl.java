package tv.accedo.one.sdk.implementation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOneDetect;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.implementation.parsers.LogLevelParser;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.implementation.utils.Request.Method;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.LogEntry;
import tv.accedo.one.sdk.model.LogLevel;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneDetectImpl extends Constants implements AccedoOneDetect {
    //Services
    private AccedoOneImpl accedoOneImpl;

    //Config
    private long loggingPeriod = DEFAULT_LOGGING_PERIOD;
    private long loglevelInvalidation = DEFAULT_LOGLEVEL_INVALIDATION_PERIOD;

    //Storage
    private Pair<LogLevel, Long> storedActiveLogLevel;
    private List<LogEntry> logEntries = Collections.synchronizedList(new ArrayList<LogEntry>());

    //Threading
    private static final int MESSAGE_PURGE = 1;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            purgeLogs();
        }
    };

    public AccedoOneDetectImpl setLoggingPeriod(long loggingPeriod) {
        this.loggingPeriod = loggingPeriod;
        return this;
    }

    public AccedoOneDetectImpl setLoglevelInvalidation(long loglevelInvalidation) {
        this.loglevelInvalidation = loglevelInvalidation;
        return this;
    }

    public AccedoOneDetectImpl(AccedoOneImpl accedoOneImpl) {
        this.accedoOneImpl = accedoOneImpl;
    }

    @Override
    public void log(LogLevel logLevel, int code, String message, String... dimensions) {
        LogEntry logEntry = new LogEntry(logLevel, accedoOneImpl.getServerTime(), code, message, dimensions);

        //Check logEntry
        if (logLevel == null || code < 0 || code > 99999 || message == null || (dimensions != null && dimensions.length > 4)) {
            Utils.log(Log.WARN, "Invalid logEntry, skipping: " + logEntry.toString());
            return;
        }

        //Queue
        logEntry.toLogCat("Queuing log: ");
        logEntries.add(logEntry);

        //Start purgehandler if necessary
        if (!handler.hasMessages(MESSAGE_PURGE)) {
            handler.sendMessageDelayed(Message.obtain(handler, MESSAGE_PURGE), loggingPeriod);
        } else if (logEntries.size() > 100) {
            handler.removeMessages(MESSAGE_PURGE);
            purgeLogs();
        }
    }

    @Override
    public Cancellable purgeLogs() {
        handler.removeMessages(MESSAGE_PURGE);

        //Copy list of things to send for concurrency reasons
        final ArrayList<LogEntry> toSend = new ArrayList<>(logEntries);

        return new CallbackAsyncTask<Void, Exception>() {
            @Override
            public Void call() throws Exception {
                //Get active log level
                LogLevel activeLogLevel = getActiveLogLevel();
                if (LogLevel.off.equals(activeLogLevel)) {
                    return null;
                }

                //Create json payload out of logs
                JSONArray payload = new JSONArray();
                for (LogEntry logEntry : toSend) {
                    if (activeLogLevel.level >= logEntry.getLogLevel().level) {
                        payload.put(logEntry.toJSONObject());
                    }
                }

                //Send
                Utils.log(Log.DEBUG, String.format("Sending out batch logs, with logLevel equals or higher \"%s\"", activeLogLevel.name()));
                accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + PATH_LOGS)
                        .setMethod(Method.POST, payload.toString())
                        .addHeader("Content-Type", "application/json")
                        .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker());

                return null;
            }

            @Override
            public void onSuccess(Void avoid) {
                logEntries.removeAll(toSend);
            }

            @Override
            public void onFailure(Exception caughtException) {
                Utils.log(caughtException);
                handler.sendMessageDelayed(Message.obtain(handler, MESSAGE_PURGE), loggingPeriod);
            }
        }.executeAndReturn();
    }

    private Cancellable logAppEvent(final String event, final Integer retentionSeconds) {
        return new CallbackAsyncTask<Void, Exception>() {
            @Override
            public Void call() throws Exception {
                String payload;
                if (retentionSeconds == null) {
                    payload = String.format("{\"eventType\" : \"%s\"}", event);
                } else {
                    payload = String.format("{\"eventType\" : \"%s\", \"retentionTime\" : %s}", event, retentionSeconds.intValue());
                }

                accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + PATH_LOG_APPEVENT)
                        .setMethod(Method.POST, payload)
                        .addHeader("Content-Type", "application/json")
                        .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker());

                return null;
            }

            @Override
            public void onFailure(Exception caughtException) {
                Utils.log(caughtException);
            }
        }.executeAndReturn();
    }

    private LogLevel getActiveLogLevel() throws AccedoOneException {
        //If we have a loglevel and its valid, return
        if (storedActiveLogLevel != null && storedActiveLogLevel.second > accedoOneImpl.getServerTime()) {
            return storedActiveLogLevel.first;
        }

        //Otherwise fetch and store
        storedActiveLogLevel = accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + PATH_LOG_LEVEL)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new LogLevelParser(loglevelInvalidation));

        //And return
        return storedActiveLogLevel.first;
    }
}
