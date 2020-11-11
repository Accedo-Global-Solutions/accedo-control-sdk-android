package tv.accedo.one.sdk.implementation.mock;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOnePublishByAlias;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOnePublishByAlias;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOnePublishByAliasImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class MockOnePublishByAlias implements AccedoOnePublishByAlias {
    private MockOnePublish mockOnePublish;

    public MockOnePublishByAlias(MockOnePublish mockOnePublish) {
        this.mockOnePublish = mockOnePublish;
    }

    @Override
    public JSONObject getEntry(Context context, String alias) throws AccedoOneException {
        if(alias==null){
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"alias\" can not be null.");
        }

        return PublishUtils.getEntryByMeta(mockOnePublish.getAllEntries(context), "entryAlias", alias);
    }

    @Override
    public JSONObject getEntry(Context context, String alias, OptionalParams optionalParams) throws AccedoOneException {
        return getEntry(context, alias);
    }

    @Override
    public JSONArray getEntries(Context context, String typeAlias) throws AccedoOneException {
        if(typeAlias==null){
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"typeAlias\" can not be null.");
        }

        return PublishUtils.getEntriesByMeta(mockOnePublish.getAllEntries(context), "typeAlias", typeAlias);
    }

    @Override
    public JSONArray getEntries(Context context, String typeAlias, OptionalParams optionalParams) throws AccedoOneException {
        return getEntries(context, typeAlias);
    }

    @Override
    public PagedResponse getEntries(Context context, String typeAlias, PaginatedParams paginatedParams) throws AccedoOneException {
        return PublishUtils.filterByPage(getEntries(context, typeAlias), paginatedParams);
    }

    @Override
    public JSONArray getEntries(Context context, List<String> aliases) throws AccedoOneException {
        if(aliases==null || aliases.isEmpty()){
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, "\"aliases\" can not be null or empty.");
        }

        return PublishUtils.getEntriesByMetas(mockOnePublish.getAllEntries(context), "entryAlias", aliases);
    }

    @Override
    public JSONArray getEntries(Context context, List<String> aliases, OptionalParams optionalParams) throws AccedoOneException {
        return getEntries(context, aliases);
    }

    @Override
    public PagedResponse getEntries(Context context, List<String> aliases, PaginatedParams paginatedParams) throws AccedoOneException {
        return PublishUtils.filterByPage(getEntries(context, aliases), paginatedParams);
    }

    @Override
    public AsyncAccedoOnePublishByAlias async() {
        return new AsyncAccedoOnePublishByAliasImpl(this);
    }
}