package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import tv.accedo.one.sdk.definition.AccedoOnePublish;
import tv.accedo.one.sdk.definition.AccedoOnePublishByAlias;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublish;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOnePublishImpl;
import tv.accedo.one.sdk.implementation.parsers.JSONObjectParser;
import tv.accedo.one.sdk.implementation.parsers.PagedResponseParser;
import tv.accedo.one.sdk.implementation.parsers.PublishLocalesParser;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;
import tv.accedo.one.sdk.model.PublishLocale;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOnePublishImpl extends Constants implements AccedoOnePublish {
    private final AccedoOneImpl accedoOneImpl;
    private final SimpleDateFormat sdf;

    public AccedoOnePublishImpl(AccedoOneImpl accedoOneImpl) {
        this.accedoOneImpl = accedoOneImpl;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        this.sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @NonNull
    @Override
    public JSONObject getEntry(@NonNull Context context, @NonNull String id) throws AccedoOneException {
        return getEntry(context, id, null);
    }

    @NonNull
    @Override
    public JSONObject getEntry(@NonNull Context context, @NonNull String id, OptionalParams optionalParams) throws AccedoOneException {
        if (id.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"id\" can not be null.");
        }

        String url = createUri(accedoOneImpl.getEndpoint() + PATH_ENTRY + id, new PaginatedParams(optionalParams)).toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new JSONObjectParser());
    }


    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final String typeId) throws AccedoOneException {
        return new PaginatedFetchAllTask() {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, typeId, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final String typeId, OptionalParams optionalParams) throws AccedoOneException {
        return new PaginatedFetchAllTask(optionalParams) {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, typeId, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public PagedResponse getEntries(@NonNull Context context, @NonNull String typeId, PaginatedParams paginatedParams) throws AccedoOneException {
        if (typeId.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"typeId\" can not be empty.");
        }

        String url = createUri(accedoOneImpl.getEndpoint() + PATH_ENTRIES, paginatedParams)
                .buildUpon()
                .appendQueryParameter("typeId", typeId)
                .build()
                .toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new PagedResponseParser());
    }


    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final List<String> ids) throws AccedoOneException {
        return new PaginatedFetchAllTask() {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, ids, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public JSONArray getEntries(@NonNull final Context context, @NonNull final List<String> ids, OptionalParams optionalParams) throws AccedoOneException {
        return new PaginatedFetchAllTask(optionalParams) {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getEntries(context, ids, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public PagedResponse getEntries(@NonNull Context context, @NonNull List<String> ids, PaginatedParams paginatedParams) throws AccedoOneException {
        if (ids.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"ids\" can not be null or empty.");
        }

        String url = createUri(accedoOneImpl.getEndpoint() + PATH_ENTRIES, paginatedParams)
                .buildUpon()
                .appendQueryParameter("id", Utils.join(ids, ","))
                .build()
                .toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new PagedResponseParser());
    }


    @NonNull
    @Override
    public JSONArray getAllEntries(@NonNull final Context context) throws AccedoOneException {
        return new PaginatedFetchAllTask() {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getAllEntries(context, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public JSONArray getAllEntries(@NonNull final Context context, OptionalParams optionalParams) throws AccedoOneException {
        return new PaginatedFetchAllTask(optionalParams) {
            @Override
            public PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException {
                return getAllEntries(context, paginatedParams);
            }
        }.fetchAll();
    }

    @NonNull
    @Override
    public PagedResponse getAllEntries(@NonNull Context context, PaginatedParams paginatedParams) throws AccedoOneException {
        String url = createUri(accedoOneImpl.getEndpoint() + PATH_ENTRIES, paginatedParams).toString();

        return accedoOneImpl.createSessionedRestClient(url)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new PagedResponseParser());
    }


    @NonNull
    @Override
    public List<PublishLocale> getAvailableLocales(@NonNull Context context) throws AccedoOneException {
        return accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + PATH_LOCALES)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new PublishLocalesParser());
    }

    Uri createUri(String url, PaginatedParams paginatedParams) throws AccedoOneException {
        Uri uri = Uri.parse(url);

        if (paginatedParams != null) {
            //Check
            if (paginatedParams.isPreview() && paginatedParams.getAt() != null) {
                throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"at\" parameter can not be used if \"preview\" is set to \"true\".");
            }
            if (paginatedParams.getOffset() < 0) {
                throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"offset\" parameter (" + paginatedParams.getOffset() + ") can not be negative.");
            }
            if (paginatedParams.getSize() < 1 || paginatedParams.getSize() > 50) {
                throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"size\" parameter (" + paginatedParams.getSize() + ") must be between 1 and 50, default is 20.");
            }

            //Append
            Builder uriBuilder = uri.buildUpon();
            uriBuilder.appendQueryParameter("preview", "" + paginatedParams.isPreview());
            uriBuilder.appendQueryParameter("offset", "" + paginatedParams.getOffset());
            uriBuilder.appendQueryParameter("size", "" + paginatedParams.getSize());
            if (paginatedParams.getAt() != null) {
                uriBuilder.appendQueryParameter("at", sdf.format(paginatedParams.getAt()));
            }
            if (!TextUtils.isEmpty(paginatedParams.getLocale())) {
                uriBuilder.appendQueryParameter("locale", paginatedParams.getLocale());
            }
            uri = uriBuilder.build();
        }

        return uri;
    }


    @NonNull
    @Override
    public AsyncAccedoOnePublish async() {
        return new AsyncAccedoOnePublishImpl(this);
    }

    @NonNull
    @Override
    public AccedoOnePublishByAlias byAlias() {
        return new AccedoOnePublishByAliasImpl(accedoOneImpl, this);
    }
}
