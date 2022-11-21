package tv.accedo.one.sdk.implementation.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import java.util.concurrent.Executor;

import tv.accedo.one.sdk.definition.async.Callback;
import tv.accedo.one.sdk.definition.async.Cancellable;

/**
 * Created by Pásztor Tibor Viktor <tibor.pasztor@accedo.tv> on 2017. 10. 09.
 */
@SuppressLint("StaticFieldLeak")
public abstract class CallbackAsyncTask<Result, E extends Exception> extends AsyncTask<Void, Void, Result> implements Cancellable {
    @Nullable
    private Callback<Result> onSuccess;
    @Nullable
    private Callback<E> onFailure;
    @Nullable
    private E caughtException;

    public CallbackAsyncTask(@Nullable Callback<Result> onSuccess, @Nullable Callback<E> onFailure) {
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    public CallbackAsyncTask() {
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute} by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates on the UI thread.
     *
     * @return A result, defined by the subclass of this task.
     * @throws Exception
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    public abstract Result call() throws Exception;

    @Override
    protected Result doInBackground(Void... params) {
        try {
            return call();
        } catch (Exception e) {
            try {
                caughtException = (E) e;
            } catch (ClassCastException classCastException) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (caughtException != null) {
            if (onFailure != null) {
                onFailure.execute(caughtException);
            }
            onFailure(caughtException);
        } else {
            if (onSuccess != null) {
                onSuccess.execute(result);
            }
            onSuccess(result);
        }
    }

    protected void onSuccess(Result result) {
    }

    protected void onFailure(E caughtException) {
    }

    public CallbackAsyncTask<Result, E> executeAndReturn() {
        execute();
        return this;
    }

    public CallbackAsyncTask<Result, E> executeAndReturn(Executor executor) {
        executeOnExecutor(executor);
        return this;
    }

    @Override
    public void cancel() {
        cancel(true);
    }
}
