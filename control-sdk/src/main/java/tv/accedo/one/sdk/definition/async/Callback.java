package tv.accedo.one.sdk.definition.async;

/**
 * Generic callback, that can be used for various async operations.
 *
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface Callback<T> {
    public void execute(T result);
}
