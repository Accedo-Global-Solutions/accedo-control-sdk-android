package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.PublishLocale;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class PublishLocalesParser implements ThrowingParser<Response, List<PublishLocale>, AccedoOneException> {
    @Override
    public List<PublishLocale> parse(Response input) throws AccedoOneException {
        try {
            ArrayList<PublishLocale> result = new ArrayList<>();
            JSONArray jsonArray = new JSONObject(input.getText()).getJSONArray("locales");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String code = jsonObject.getString("code");
                String displayName = jsonObject.getString("displayName");

                result.add(new PublishLocale(code, displayName));
            }

            return result;
        } catch (Exception e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }
}