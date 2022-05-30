package tv.accedo.one.sdk.implementation;

import tv.accedo.one.sdk.definition.AccedoOneInsight;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.implementation.utils.Request.Method;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneInsightImpl extends Constants implements AccedoOneInsight {
    private AccedoOneImpl accedoOneImpl;

    public AccedoOneInsightImpl(AccedoOneImpl accedoOneImpl) {
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
                        .setMethod(Method.POST,payload)
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
