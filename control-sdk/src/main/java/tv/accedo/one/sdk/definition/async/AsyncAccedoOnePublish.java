/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.definition.async;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;
import tv.accedo.one.sdk.model.PublishLocale;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AsyncAccedoOnePublish {
    /**
     * @param context
     * @param id        can not be null
     * @param onSuccess Returns a single entry defined by the given id
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntry(@NonNull Context context, @NonNull String id, @Nullable Callback<JSONObject> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param id             can not be null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a single entry defined by the given id
     * @param onFailure      Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntry(@NonNull Context context, @NonNull String id, OptionalParams optionalParams, @Nullable Callback<JSONObject> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeId    can not be null
     * @param onSuccess Returns a list of all the entries defined by the given typeId. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull String typeId, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeId         can not be null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a list of all the entries defined by the given typeId. This call is not paginated.
     * @param onFailure      Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull String typeId, OptionalParams optionalParams, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeId          can not be null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess       Returns a paged list of all the entries defined by the given typeId.
     * @param onFailure       Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull String typeId, PaginatedParams paginatedParams, @Nullable Callback<PagedResponse> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param ids       can not be empty or null
     * @param onSuccess Returns a list of entries defined by a list of ids. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull List<String> ids, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param ids            can not be empty or null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a list of entries defined by a list of ids. This call is not paginated.
     * @param onFailure      Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull List<String> ids, OptionalParams optionalParams, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param ids             can not be empty or null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess       Returns a paged list of entries defined by a list of ids.
     * @param onFailure       Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull List<String> ids, PaginatedParams paginatedParams, @Nullable Callback<PagedResponse> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param onSuccess Returns a list of all the entries. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllEntries(@NonNull Context context, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a list of all the entries. This call is not paginated.
     * @param onFailure      Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllEntries(@NonNull Context context, OptionalParams optionalParams, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess       Returns a paged list of all the entries.
     * @param onFailure       Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllEntries(@NonNull Context context, PaginatedParams paginatedParams, @Nullable Callback<PagedResponse> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @return All available locales for the Organization.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    Cancellable getAvailableLocales(@NonNull Context context, @Nullable Callback<List<PublishLocale>> onSuccess, @Nullable Callback<AccedoOneException> onFailure);
}
