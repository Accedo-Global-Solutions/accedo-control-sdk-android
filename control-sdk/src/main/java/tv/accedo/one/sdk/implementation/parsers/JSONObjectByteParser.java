package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONObject;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class JSONObjectByteParser implements ThrowingParser<byte[], JSONObject, AccedoOneException> {
    @Override
    public JSONObject parse(byte[] input) throws AccedoOneException {
        try {
            return new JSONObject(new String(input, "UTF-8"));
        } catch (Exception e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }
}