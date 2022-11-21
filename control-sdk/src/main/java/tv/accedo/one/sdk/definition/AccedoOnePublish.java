package tv.accedo.one.sdk.definition;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublish;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.PublishLocale;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOnePublish {
    /**
     * @param context
     * @param id can not be null
     * @return A single entry defined by the given id
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONObject getEntry(@NonNull Context context, @NonNull String id) throws AccedoOneException;

    /**
     * @param context
     * @param id can not be null
     * @param optionalParams contains extra parameters for the call
     * @return A single entry defined by the given id
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONObject getEntry(@NonNull Context context,@NonNull  String id, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param typeId can not be null
     * @return A list of all the entries defined by the given typeId. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context,@NonNull  String typeId) throws AccedoOneException;

    /**
     * @param context
     * @param typeId can not be null
     * @param optionalParams contains extra parameters for the call
     * @return A list of all the entries defined by the given typeId. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context,@NonNull String typeId, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param typeId can not be null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @return A paged list of all the entries defined by the given typeId.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    PagedResponse getEntries(@NonNull Context context,@NonNull  String typeId, @Nullable PaginatedParams paginatedParams) throws AccedoOneException;

    /**
     * @param context
     * @param ids can not be empty or null
     * @return A list of entries defined by a list of ids. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context, @NonNull List<String> ids) throws AccedoOneException;

    /**
     * @param context
     * @param ids can not be empty or null
     * @param optionalParams contains extra parameters for the call
     * @return A list of entries defined by a list of ids. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context, @NonNull List<String> ids, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param ids can not be empty or null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @return A paged list of entries defined by a list of ids.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    PagedResponse getEntries(@NonNull Context context,@NonNull  List<String> ids, @Nullable PaginatedParams paginatedParams) throws AccedoOneException;

    /**
     * @param context
     * @return A list of all the entries. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getAllEntries(@NonNull Context context) throws AccedoOneException;

    /**
     * @param context
     * @param optionalParams contains extra parameters for the call
     * @return A list of all the entries. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getAllEntries(@NonNull Context context, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @return A paged list of all the entries.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    PagedResponse getAllEntries(@NonNull Context context, @Nullable PaginatedParams paginatedParams) throws AccedoOneException;

    /**
     * @param context
     * @return All available locales for the Organization.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    List<PublishLocale> getAvailableLocales(@NonNull Context context) throws AccedoOneException;

    /**
     * Used for connecting asynchronously, using callbacks.
     * @return an {@link AsyncAccedoOnePublish} instance, containing this {@link AccedoOnePublish} instance
     */
    @NonNull
    AsyncAccedoOnePublish async();

    /**
     * Used to fetch Publish entries by alias.
     * @return an {@link AccedoOnePublishByAlias} instance.
     */
    @NonNull
    AccedoOnePublishByAlias byAlias();
}
