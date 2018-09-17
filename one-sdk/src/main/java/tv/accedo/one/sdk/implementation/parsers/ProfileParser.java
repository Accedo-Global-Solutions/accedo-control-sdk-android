package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONObject;

import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.Profile;
import tv.accedo.one.sdk.implementation.utils.Response;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class ProfileParser implements ThrowingParser<Response, Profile, AccedoOneException> {
    @Override
    public Profile parse(Response response) throws AccedoOneException {
        try {
            return Profile.fromJson(new JSONObject(response.getText()));
        } catch (Exception e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }
}