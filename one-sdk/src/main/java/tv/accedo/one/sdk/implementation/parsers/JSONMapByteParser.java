package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class JSONMapByteParser implements ThrowingParser<byte[], Map<String, String>, AccedoOneException> {
    @Override
    public Map<String, String> parse(byte[] rawResponse) throws AccedoOneException {
        try {
            HashMap<String, String> result = new HashMap<String, String>();
            JSONObject jsonObject = new JSONObject(new String(rawResponse, "UTF-8"));
            for(Iterator<String> iterator = jsonObject.keys(); iterator.hasNext();){
                String key = iterator.next();
                result.put(key, jsonObject.getString(key));
            }
            
            return result;
        } catch (Exception e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }
}