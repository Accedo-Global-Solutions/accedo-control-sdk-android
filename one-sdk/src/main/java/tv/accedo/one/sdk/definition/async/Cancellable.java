package tv.accedo.one.sdk.definition.async;

/**
 * Generic interface defined for cancellable operations, such as AsyncTasks, SafeAsyncTasks, CancellableAsyncTasks.
 *
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface Cancellable {
    public void cancel();
}
