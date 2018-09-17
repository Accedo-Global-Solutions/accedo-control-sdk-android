package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.implementation.utils.Response;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class PagedResponseParser implements ThrowingParser<Response, PagedResponse, AccedoOneException> {
    @Override
    public PagedResponse parse(Response response) throws AccedoOneException {
        try {
            JSONObject jsonObject = new JSONObject(response.getText());
            JSONArray jsonArrayEntries = jsonObject.getJSONArray("entries");
            JSONObject jsonObjectPagination = jsonObject.getJSONObject("pagination");

            int total = jsonObjectPagination.getInt("total");
            int size = jsonObjectPagination.getInt("size");
            int offset = jsonObjectPagination.getInt("offset");

            return new PagedResponse(jsonArrayEntries, total, size, offset);
        } catch (Exception e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }
}