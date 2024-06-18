package tv.accedo.one.sdk.implementation;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Locale;

import androidx.annotation.NonNull;

import tv.accedo.one.sdk.definition.AccedoOneInsight;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.implementation.utils.Request.Method;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneInsightImpl extends Constants implements AccedoOneInsight {
    @NonNull
    private final AccedoOneImpl accedoOneImpl;

    public AccedoOneInsightImpl(@NonNull AccedoOneImpl accedoOneImpl) {
        this.accedoOneImpl = accedoOneImpl;
    }

    @Override
    public Cancellable applicationStart() {
        return logAppEvent(EVENT_START, null);
    }

    @Override
    public Cancellable applicationQuit() {
        return logAppEvent(EVENT_QUIT, null);
    }

    @Override
    public Cancellable applicationQuit(final int retentionSeconds) {
        return logAppEvent(EVENT_QUIT, retentionSeconds);
    }

    @SuppressLint("StaticFieldLeak")
    private Cancellable logAppEvent(@NonNull final String event, final Integer retentionSeconds) {
        return new CallbackAsyncTask<Void, Exception>() {
            @Override
            public Void call() throws Exception {
                String payload;
                if (retentionSeconds == null) {
                    payload = String.format("{\"eventType\" : \"%s\"}", event);
                } else {
                    payload = String.format(Locale.getDefault(),"{\"eventType\" : \"%s\", \"retentionTime\" : %d}", event, retentionSeconds);
                }

                Utils.log(Log.DEBUG, String.format(Locale.getDefault(),
                        "logAppEvent(event %s, retentionSeconds %d \n payload is: %s", event, retentionSeconds, payload));
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
}
