package tv.accedo.one.sdk.implementation.async;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOnePublish;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublish;
import tv.accedo.one.sdk.definition.async.Callback;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;
import tv.accedo.one.sdk.model.PublishLocale;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AsyncAccedoOnePublishImpl implements AsyncAccedoOnePublish {
    @NonNull
    private final AccedoOnePublish accedoOnePublish;

    public AsyncAccedoOnePublishImpl(@NonNull AccedoOnePublish accedoOnePublish) {
        this.accedoOnePublish = accedoOnePublish;
    }


    @Override
    public Cancellable getEntry(@NonNull final Context context, @NonNull final String id, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONObject, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONObject call() throws Exception {
                return accedoOnePublish.getEntry(context, id);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntry(@NonNull final Context context, @NonNull final String id, final OptionalParams optionalParams, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONObject, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONObject call() throws Exception {
                return accedoOnePublish.getEntry(context, id, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final String typeId, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublish.getEntries(context, typeId);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final String typeId, final OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublish.getEntries(context, typeId, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final String typeId, final PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<PagedResponse, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public PagedResponse call() throws Exception {
                return accedoOnePublish.getEntries(context, typeId, paginatedParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final List<String> ids, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublish.getEntries(context, ids);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final List<String> ids, final OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublish.getEntries(context, ids, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final List<String> ids, final PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<PagedResponse, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public PagedResponse call() throws Exception {
                return accedoOnePublish.getEntries(context, ids, paginatedParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllEntries(@NonNull final Context context, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublish.getAllEntries(context);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllEntries(@NonNull final Context context, final OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublish.getAllEntries(context, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAllEntries(@NonNull final Context context, final PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<PagedResponse, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public PagedResponse call() throws Exception {
                return accedoOnePublish.getAllEntries(context, paginatedParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getAvailableLocales(@NonNull final Context context, Callback<List<PublishLocale>> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<List<PublishLocale>, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public List<PublishLocale> call() throws Exception {
                return accedoOnePublish.getAvailableLocales(context);
            }
        }.executeAndReturn();
    }
}
