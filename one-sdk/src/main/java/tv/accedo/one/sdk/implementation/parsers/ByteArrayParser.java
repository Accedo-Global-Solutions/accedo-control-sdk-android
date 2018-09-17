package tv.accedo.one.sdk.implementation.parsers;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class ByteArrayParser implements ThrowingParser<byte[], byte[], AccedoOneException> {
    @Override
    public byte[] parse(byte[] input) throws AccedoOneException {
        return input;
    }
}