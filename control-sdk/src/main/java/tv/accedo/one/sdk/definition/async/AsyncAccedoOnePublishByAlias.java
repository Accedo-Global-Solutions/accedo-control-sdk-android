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

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AsyncAccedoOnePublishByAlias {
    /**
     * @param context
     * @param alias     can not be null
     * @param onSuccess Returns a single entry defined by the given alias
     * @param onFailure Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntry(@NonNull Context context, @NonNull String alias, @Nullable Callback<JSONObject> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param alias          can not be null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a single entry defined by the given alias
     * @param onFailure      Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntry(@NonNull Context context, @NonNull String alias, OptionalParams optionalParams, @Nullable Callback<JSONObject> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeAlias can not be null
     * @param onSuccess Returns a list of all the entries defined by the given typeAlias. This call is not paginated.
     * @param onFailure Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull String typeAlias,@Nullable  Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeAlias      can not be null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a list of all the entries defined by the given typeAlias. This call is not paginated.
     * @param onFailure      Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull String typeAlias, OptionalParams optionalParams, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeAlias       can not be null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess       Returns a paged list of all the entries defined by the given typeAlias.
     * @param onFailure       Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull String typeAlias, PaginatedParams paginatedParams, @Nullable Callback<PagedResponse> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param aliases   can not be empty or null
     * @param onSuccess Returns a list of entries defined by a list of aliases. This call is not paginated.
     * @param onFailure Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull List<String> aliases, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param aliases        can not be empty or null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess      Returns a list of entries defined by a list of aliases. This call is not paginated.
     * @param onFailure      Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull List<String> aliases, OptionalParams optionalParams, @Nullable Callback<JSONArray> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param aliases         can not be empty or null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess       Returns a paged list of entries defined by a list of aliases.
     * @param onFailure       Returns an {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(@NonNull Context context, @NonNull List<String> aliases, PaginatedParams paginatedParams, @Nullable Callback<PagedResponse> onSuccess, @Nullable Callback<AccedoOneException> onFailure);
}
