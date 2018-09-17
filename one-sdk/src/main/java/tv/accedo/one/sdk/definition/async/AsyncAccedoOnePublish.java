/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.definition.async;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.PublishLocale;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AsyncAccedoOnePublish {
    /**
     * @param context
     * @param id can not be null
     * @param onSuccess Returns a single entry defined by the given id
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntry(Context context, String id, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param id can not be null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess Returns a single entry defined by the given id
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntry(Context context, String id, OptionalParams optionalParams, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeId can not be null
     * @param onSuccess Returns a list of all the entries defined by the given typeId. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(Context context, String typeId, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeId can not be null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess Returns a list of all the entries defined by the given typeId. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(Context context, String typeId, OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param typeId can not be null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess Returns a paged list of all the entries defined by the given typeId.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(Context context, String typeId, PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param ids can not be empty or null
     * @param onSuccess Returns a list of entries defined by a list of ids. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(Context context, List<String> ids, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param ids can not be empty or null
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess Returns a list of entries defined by a list of ids. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(Context context, List<String> ids, OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param ids can not be empty or null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess Returns a paged list of entries defined by a list of ids.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getEntries(Context context, List<String> ids, PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param onSuccess Returns a list of all the entries. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllEntries(Context context, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param optionalParams contains extra parameters for the call
     * @param onSuccess Returns a list of all the entries. This call is not paginated.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllEntries(Context context, OptionalParams optionalParams, Callback<JSONArray> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @param onSuccess Returns a paged list of all the entries.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode on what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllEntries(Context context, PaginatedParams paginatedParams, Callback<PagedResponse> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @return All available locales for the Organization.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    Cancellable getAvailableLocales(Context context, Callback<List<PublishLocale>> onSuccess, Callback<AccedoOneException> onFailure);
}