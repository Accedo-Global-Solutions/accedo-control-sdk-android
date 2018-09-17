package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONObject;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.implementation.utils.Response;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class JSONObjectParser implements ThrowingParser<Response, JSONObject, AccedoOneException> {
    @Override
    public JSONObject parse(Response input) throws AccedoOneException {
        return new JSONObjectByteParser().parse(input.getRawResponse());
    }
}