package tv.accedo.one.sdk.implementation;

import org.json.JSONArray;
import org.json.JSONException;

import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
abstract class PaginatedFetchAllTask {
    private OptionalParams optionalParams = null;

    public PaginatedFetchAllTask() {
    }

    public PaginatedFetchAllTask(OptionalParams optionalParams) {
        this.optionalParams = optionalParams;
    }

    public abstract PagedResponse fetchPage(PaginatedParams paginatedParams) throws AccedoOneException;

    public JSONArray fetchAll() throws AccedoOneException {
        JSONArray result = new JSONArray();
        PaginatedParams paginatedParams = new PaginatedParams(optionalParams).setSize(PaginatedParams.MAX_PAGE_SIZE);
        PagedResponse pagedResponse = null;

        while (hasMore(pagedResponse)) {
            pagedResponse = fetchPage(paginatedParams);
            addAll(result, pagedResponse.getEntries());
            paginatedParams.setOffset(paginatedParams.getOffset() + 1);
        }

        return result;
    }

    private boolean hasMore(PagedResponse pagedResponse){
        return pagedResponse == null || pagedResponse.getOffset() * pagedResponse.getSize() + pagedResponse.getEntries().length() < pagedResponse.getTotal();
    }

    private void addAll(JSONArray toAddTo, JSONArray toAdd) throws AccedoOneException {
        for (int i = 0; i < toAdd.length(); i++) {
            try {
                toAddTo.put(toAdd.get(i));
            } catch (JSONException e) {
                throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
            }
        }
    }
}