package tv.accedo.one.sdk.implementation.async;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOnePublishByAlias;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublishByAlias;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;
import tv.accedo.one.sdk.implementation.utils.CallbackAsyncTask;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.definition.async.Callback;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AsyncAccedoOnePublishByAliasImpl implements AsyncAccedoOnePublishByAlias {
    @NonNull
    private final AccedoOnePublishByAlias accedoOnePublishByAlias;

    public AsyncAccedoOnePublishByAliasImpl(@NonNull AccedoOnePublishByAlias accedoOnePublishByAlias) {
        this.accedoOnePublishByAlias = accedoOnePublishByAlias;
    }

    @Override
    public Cancellable getEntry(@NonNull final Context context, @NonNull final String alias, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONObject, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONObject call() throws Exception {
                return accedoOnePublishByAlias.getEntry(context, alias);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntry(@NonNull final Context context, @NonNull final String alias, final OptionalParams optionalParams, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONObject, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONObject call() throws Exception {
                return accedoOnePublishByAlias.getEntry(context, alias, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final String typeAlias, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublishByAlias.getEntries(context, typeAlias);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final String typeAlias, final OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublishByAlias.getEntries(context, typeAlias, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final String typeAlias, final PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<PagedResponse, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public PagedResponse call() throws Exception {
                return accedoOnePublishByAlias.getEntries(context, typeAlias, paginatedParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final List<String> aliases, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublishByAlias.getEntries(context, aliases);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final List<String> aliases, final OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<JSONArray, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public JSONArray call() throws Exception {
                return accedoOnePublishByAlias.getEntries(context, aliases, optionalParams);
            }
        }.executeAndReturn();
    }

    @Override
    public Cancellable getEntries(@NonNull final Context context, @NonNull final List<String> aliases, final PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure) {
        return new CallbackAsyncTask<PagedResponse, AccedoOneException>(onSuccess, onFailure) {
            @Override
            public PagedResponse call() throws Exception {
                return accedoOnePublishByAlias.getEntries(context, aliases, paginatedParams);
            }
        }.executeAndReturn();
    }
}
