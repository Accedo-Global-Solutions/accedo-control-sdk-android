package tv.accedo.one.sdk.implementation;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOnePublishByAlias;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublishByAlias;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOnePublishByAliasImpl;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;
import tv.accedo.one.sdk.implementation.parsers.JSONObjectParser;
import tv.accedo.one.sdk.implementation.parsers.PagedResponseParser;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOnePublishByAliasImpl extends Constants implements AccedoOnePublishByAlias {
    private final AccedoOneImpl accedoOneImpl;
    private final AccedoOnePublishImpl accedoOnePublishImpl;

    public AccedoOnePublishByAliasImpl(AccedoOneImpl accedoOneImpl, AccedoOnePublishImpl accedoOnePublishImpl) {
        this.accedoOneImpl = accedoOneImpl;
        this.accedoOnePublishImpl = accedoOnePublishImpl;
    }

    @NonNull
    @Override
    public JSONObject getEntry(@NonNull Context context, @NonNull String alias) throws AccedoOneException {
        return getEntry(context, alias, null);
    }

    @NonNull
    @Override
    public JSONObject getEntry(@NonNull Context context, @NonNull String alias, OptionalParams optionalParams) throws AccedoOneException {
        if (alias.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"alias\" can not be null.");
        }

        String url = accedoOnePublishImpl.createUri(accedoOneImpl.getEndpoint() + PATH_ENTRY_BY_ALIAS + alias, new PaginatedParams(optionalParams)).toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new JSONObjectParser());
    }

    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final String typeAlias) throws AccedoOneException {
        return new PaginatedFetchAllTask() {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, typeAlias, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final String typeAlias, OptionalParams optionalParams) throws AccedoOneException {
        return new PaginatedFetchAllTask(optionalParams) {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, typeAlias, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public PagedResponse getEntries(@NonNull Context context, @NonNull String typeAlias, PaginatedParams paginatedParams) throws AccedoOneException {
        if (typeAlias.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"typeAlias\" can not be null.");
        }

        String url = accedoOnePublishImpl.createUri(accedoOneImpl.getEndpoint() + PATH_ENTRIES, paginatedParams)
                .buildUpon()
                .appendQueryParameter("typeAlias", typeAlias)
                .build()
                .toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new PagedResponseParser());
    }

    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final List<String> aliases) throws AccedoOneException {
        return new PaginatedFetchAllTask() {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, aliases, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final List<String> aliases, OptionalParams optionalParams) throws AccedoOneException {
        return new PaginatedFetchAllTask(optionalParams) {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, aliases, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public PagedResponse getEntries(@NonNull Context context, @NonNull List<String> aliases, PaginatedParams paginatedParams) throws AccedoOneException {
        if (aliases.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"aliases\" can not be null or empty.");
        }

        String url = accedoOnePublishImpl.createUri(accedoOneImpl.getEndpoint() + PATH_ENTRIES, paginatedParams)
                .buildUpon()
                .appendQueryParameter("alias", Utils.join(aliases, ","))
                .build()
                .toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new PagedResponseParser());
    }

    @NonNull
    @Override
    public AsyncAccedoOnePublishByAlias async() {
        return new AsyncAccedoOnePublishByAliasImpl(this);
    }
}
