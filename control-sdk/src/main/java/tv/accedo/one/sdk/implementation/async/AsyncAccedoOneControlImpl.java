package tv.accedo.one.sdk.implementation.async;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.definition.AccedoOneControl;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOneControl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.ApplicationStatus;
import tv.accedo.one.sdk.model.Profile;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.definition.async.Callback;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AsyncAccedoOneControlImpl implements AsyncAccedoOneControl {
    @NonNull
    private final AccedoOneControl accedoOneControl;

    public AsyncAccedoOneControlImpl(@NonNull AccedoOneControl accedoOneControl) {
        this.accedoOneControl = accedoOneControl;
    }

    @Override
    public Cancellable getMetadata(@NonNull final Context context, @NonNull final String key, Callback<String> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<String, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public String call() throws Exception {
                return accedoOneControl.getMetadata(context, key);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllMetadata(@NonNull final Context context, Callback<Map<String, String>> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Map<String, String>, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Map<String, String> call() throws Exception {
                return accedoOneControl.getAllMetadata(context);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllMetadataRaw(@NonNull final Context context, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONObject, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONObject call() throws Exception {
                return accedoOneControl.getAllMetadataRaw(context);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAsset(@NonNull final Context context, @NonNull final String key, Callback<byte[]> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<byte[], AccedoOneException>(onSuccess, onFailure) {
            @Override
            public byte[] call() throws Exception {
                return accedoOneControl.getAsset(context, key);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllAssets(@NonNull final Context context, Callback<Map<String, String>> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Map<String, String>, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Map<String, String> call() throws Exception {
                return accedoOneControl.getAllAssets(context);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllAssetsRaw(@NonNull final Context context, Callback<Map<String, byte[]>> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Map<String, byte[]>, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Map<String, byte[]> call() throws Exception {
                return accedoOneControl.getAllAssetsRaw(context);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getProfile(@NonNull final Context context, Callback<Profile> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<Profile, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public Profile call() throws Exception {
                return accedoOneControl.getProfile(context);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getApplicationStatus(@NonNull final Context context, Callback<ApplicationStatus> onSuccess) {
        return new CallbackAsyncTask<ApplicationStatus, AccedoOneException>(onSuccess, null) {
            @Override
            public ApplicationStatus call() throws Exception {
                return accedoOneControl.getApplicationStatus(context);
            }
        }.executeAndReturn();
    }
}
