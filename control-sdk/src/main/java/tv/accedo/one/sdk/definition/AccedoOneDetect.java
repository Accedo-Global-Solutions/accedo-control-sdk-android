package tv.accedo.one.sdk.definition;

import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.model.LogLevel;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOneDetect {
    /**
     * @param code An internal code that is meaningful for developers. Usually a generic error code. Max 5 digits.
     * @param message A text message for this log event. Messages should not exceed 10,000 characters. Longer messages will be truncated.
     * @param dimensions An optional list of up to four different dimensions. The possible values for each dimension must be predefined in the Accedo Onec Admin UI.
     */
    void log(LogLevel logLevel, int code, String message, String... dimensions);

    /**
     * Sends out all pending logs. Call this when leaving the app.
     */
    Cancellable purgeLogs();
}