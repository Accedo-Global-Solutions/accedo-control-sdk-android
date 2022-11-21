package tv.accedo.one.sdk.implementation.async;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOneUserData;
import tv.accedo.one.sdk.definition.AccedoOneUserData.Scope;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOneUser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.definition.async.Callback;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AsyncAccedoOneUserImpl implements AsyncAccedoOneUser {
    @NonNull
    private final AccedoOneUserData accedoOneUserData;

    public AsyncAccedoOneUserImpl(@NonNull AccedoOneUserData accedoOneUserData) {
        this.accedoOneUserData = accedoOneUserData;
    }

    @Override
    public Cancellable getAllUserData(@NonNull final Context context, final Scope scope, @NonNull final String userId, Callback<Map<String, String>> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Map<String, String>, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Map<String, String> call() throws Exception {
                return accedoOneUserData.getAllUserData(context, scope, userId);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable setAllUserData(@NonNull final Context context, final Scope scope, @NonNull final String userId, final Map<String, String> userData, Callback<Void> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Void, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Void call() throws Exception {
                accedoOneUserData.setAllUserData(context, scope, userId, userData);
                return null;
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getUserData(@NonNull final Context context, final Scope scope, final String userId, final String key, Callback<String> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<String, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public String call() throws Exception {
                return accedoOneUserData.getUserData(context, scope, userId, key);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable setUserData(@NonNull final Context context, final Scope scope, final String userId, final String key, final String value, Callback<Void> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Void, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Void call() throws Exception {
                accedoOneUserData.setUserData(context, scope, userId, key, value);
                return null;
            }
        }.executeAndReturn();
    }
}
