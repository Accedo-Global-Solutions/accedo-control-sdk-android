package tv.accedo.one.sdk.implementation.mock;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOnePublishByAlias;
import tv.accedo.one.sdk.definition.AccedoOnePublish;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublish;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOnePublishImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.PublishLocale;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class MockOnePublish implements AccedoOnePublish {
    private final ConfigSource configSource;

    private JSONArray entries;

    public MockOnePublish(ConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public JSONObject getEntry(Context context, String id) throws AccedoOneException {
        if (id == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"id\" can not be null.");
        }

        return PublishUtils.getEntryByMeta(getAllEntries(context), "id", id);
    }

    @Override
    public JSONObject getEntry(Context context, String id, OptionalParams optionalParams) throws AccedoOneException {
        return getEntry(context, id);
    }

    @Override
    public JSONArray getEntries(Context context, String typeId) throws AccedoOneException {
        if (typeId == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"typeId\" can not be null.");
        }

        return PublishUtils.getEntriesByMeta(getAllEntries(context), "typeId", typeId);
    }

    @Override
    public JSONArray getEntries(Context context, String typeId, OptionalParams optionalParams) throws AccedoOneException {
        return getEntries(context, typeId);
    }

    @Override
    public PagedResponse getEntries(Context context, String typeId, PaginatedParams paginatedParams) throws AccedoOneException {
        return PublishUtils.filterByPage(getEntries(context, typeId), paginatedParams);
    }

    @Override
    public JSONArray getEntries(Context context, List<String> ids) throws AccedoOneException {
        if (ids == null || ids.isEmpty()) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"ids\" can not be null or empty.");
        }

        return PublishUtils.getEntriesByMetas(getAllEntries(context), "id", ids);
    }

    @Override
    public JSONArray getEntries(Context context, List<String> ids, OptionalParams optionalParams) throws AccedoOneException {
        return getEntries(context, ids);
    }

    @Override
    public PagedResponse getEntries(Context context, List<String> ids, PaginatedParams paginatedParams) throws AccedoOneException {
        return PublishUtils.filterByPage(getEntries(context, ids), paginatedParams);
    }

    @Override
    public JSONArray getAllEntries(Context context, OptionalParams optionalParams) throws AccedoOneException {
        return getAllEntries(context);
    }

    @Override
    public PagedResponse getAllEntries(Context context, PaginatedParams paginatedParams) throws AccedoOneException {
        return PublishUtils.filterByPage(getAllEntries(context), paginatedParams);
    }

    @Override
    public List<PublishLocale> getAvailableLocales(Context context) throws AccedoOneException {
        return new ArrayList<>();
    }

    @Override
    public JSONArray getAllEntries(Context context) throws AccedoOneException {
        if (entries == null) {
            try {
                entries = configSource.getAllEntries(context);
            } catch (Exception e) {
                throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
            }
        }

        return entries;
    }

    @Override
    public AsyncAccedoOnePublish async() {
        return new AsyncAccedoOnePublishImpl(this);
    }

    @Override
    public AccedoOnePublishByAlias byAlias() {
        return new MockOnePublishByAlias(this);
    }
}