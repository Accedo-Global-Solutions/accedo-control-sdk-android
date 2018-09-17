package tv.accedo.one.sdk.implementation.mock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class PublishUtils {
    public static JSONObject getEntryByMeta(JSONArray allEntries, String metaKey, String metaValue) throws AccedoOneException {
        JSONObject jsonObject = getEntriesByMeta(allEntries, metaKey, metaValue).optJSONObject(0);

        if (jsonObject == null) {
            throw new AccedoOneException(StatusCode.NO_RESPONSE);
        }

        return jsonObject;
    }

    public static JSONArray getEntriesByMeta(JSONArray allEntries, String metaKey, String metaValue) throws AccedoOneException {
        JSONArray result = new JSONArray();
        for(int i=0; i<allEntries.length(); i++){
            JSONObject entry = allEntries.optJSONObject(i);
            if(metaValue.equals(getEntryMeta(entry, metaKey))){
                result.put(entry);
            }
        }
        return result;
    }

    public static JSONArray getEntriesByMetas(JSONArray allEntries, String metaKey, List<String> metaValues) throws AccedoOneException {
        JSONArray result = new JSONArray();
        for(int i=0; i<allEntries.length(); i++){
            JSONObject entry = allEntries.optJSONObject(i);
            if(metaValues.contains(getEntryMeta(entry, metaKey))){
                result.put(entry);
            }
        }
        return result;
    }

    public static String getEntryMeta(JSONObject jsonObject, String key) throws AccedoOneException {
        if (jsonObject == null) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE);
        }

        JSONObject meta = jsonObject.optJSONObject("_meta");
        if (meta == null) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE);
        }

        String id = meta.optString(key);
        if (id == null) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE);
        }

        return id;
    }

    public static PagedResponse filterByPage(JSONArray jsonArray, PaginatedParams paginatedParams) throws AccedoOneException {
        int from = paginatedParams.getOffset();
        int to = Math.min(paginatedParams.getOffset() + paginatedParams.getSize(), jsonArray.length());

        JSONArray result = new JSONArray();
        for (int i = from; i < to; i++) {
            try {
                result.put(jsonArray.get(i));
            } catch (JSONException e) {
                throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
            }
        }

        return new PagedResponse(result, result.length(), paginatedParams.getSize(), paginatedParams.getOffset());
    }
}