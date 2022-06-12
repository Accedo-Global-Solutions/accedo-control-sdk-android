package tv.accedo.one.sdk.definition;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublishByAlias;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOnePublishByAlias {
    /**
     * @param context
     * @param alias   can not be null
     * @return A single entry defined by the given alias
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONObject getEntry(@NonNull Context context, @NonNull String alias) throws AccedoOneException;

    /**
     * @param context
     * @param alias          can not be null
     * @param optionalParams contains extra parameters for the call
     * @return A single entry defined by the given alias
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONObject getEntry(@NonNull Context context, @NonNull String alias, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param typeAlias can not be null
     * @return A list of all the entries defined by the given typeAlias. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context, @NonNull String typeAlias) throws AccedoOneException;

    /**
     * @param context
     * @param typeAlias      can not be null
     * @param optionalParams contains extra parameters for the call
     * @return A list of all the entries defined by the given typeAlias. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context, @NonNull String typeAlias, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param typeAlias       can not be null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @return A paged list of all the entries defined by the given typeAlias.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    PagedResponse getEntries(@NonNull Context context, @NonNull String typeAlias, @Nullable PaginatedParams paginatedParams) throws AccedoOneException;

    /**
     * @param context
     * @param aliases can not be empty or null
     * @return A list of entries defined by a list of aliases. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context, @NonNull List<String> aliases) throws AccedoOneException;

    /**
     * @param context
     * @param aliases        can not be empty or null
     * @param optionalParams contains extra parameters for the call
     * @return A list of entries defined by a list of aliases. This call is not paginated.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONArray getEntries(@NonNull Context context, @NonNull List<String> aliases, @Nullable OptionalParams optionalParams) throws AccedoOneException;

    /**
     * @param context
     * @param aliases         can not be empty or null
     * @param paginatedParams contains extra parameters and pagination info for the call
     * @return A paged list of entries defined by a list of aliases.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    PagedResponse getEntries(@NonNull Context context, @NonNull List<String> aliases, @Nullable PaginatedParams paginatedParams) throws AccedoOneException;

    /**
     * Used for connecting asynchronously, using callbacks.
     *
     * @return an {@link AsyncAccedoOnePublishByAlias} instance, containing this {@link AccedoOnePublishByAlias} instance
     */
    @NonNull
    AsyncAccedoOnePublishByAlias async();
}
