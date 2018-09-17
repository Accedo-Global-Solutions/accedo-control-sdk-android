package tv.accedo.one.sdk.implementation.parsers;

import java.util.Map;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.implementation.utils.Response;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class JSONMapParser implements ThrowingParser<Response, Map<String, String>, AccedoOneException> {
    @Override
    public Map<String, String> parse(Response input) throws AccedoOneException {
        return new JSONMapByteParser().parse(input.getRawResponse());
    }
}