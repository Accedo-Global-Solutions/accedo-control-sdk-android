package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Map.Entry;

import tv.accedo.one.sdk.definition.AccedoOneUserData;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOneUser;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOneUserImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.implementation.parsers.JSONMapParser;
import tv.accedo.one.sdk.implementation.utils.Request.Method;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneUserDataImpl implements AccedoOneUserData {
    private static final String PATH_USER_APPLICATION_DATA = "/user/";
    private static final String PATH_USER_APPLICATIONGROUP_DATA = "/group/";

    private final AccedoOneImpl accedoOneImpl;

    public AccedoOneUserDataImpl(AccedoOneImpl accedoOneImpl) {
        this.accedoOneImpl = accedoOneImpl;
    }

    @Override
    public Map<String, String> getAllUserData(Context context, Scope scope, String userId) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || scope == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Call
        return accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId)
                .connect(new AccedoOneResponseChecker())
                .getParsedText(new JSONMapParser());
    }

    @Override
    public void setAllUserData(Context context, Scope scope, String userId, Map<String, String> userData) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || scope == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Make payload
        JSONObject jsonObject = new JSONObject();
        try {
            if (userData != null) {
                for(Entry<String, String> entry : userData.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (JSONException e) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, e); //Probably never happens
        }

        //Call
        accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId)
                .setMethod(Method.POST)
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .setPayload(jsonObject.toString())
                .connect(new AccedoOneResponseChecker());
    }

    @Override
    public String getUserData(Context context, Scope scope, String userId, String key) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || scope == null || key == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Call
        return accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId + "/" + key)
                .connect(new AccedoOneResponseChecker())
                .getText();
    }

    @Override
    public void setUserData(Context context, Scope scope, String userId, String key, String value) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || key == null || value == null || scope == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Call
        accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId + "/" + key)
                .setMethod(Method.POST)
                .setHeader("Content-Type", "text/plain; charset=utf-8")
                .setPayload(value)
                .connect(new AccedoOneResponseChecker());
    }

    private String getPathForScope(Scope scope){
        return Scope.APPLICATION.equals(scope)? PATH_USER_APPLICATION_DATA : PATH_USER_APPLICATIONGROUP_DATA;
    }

    @Override
    public AsyncAccedoOneUser async() {
        return new AsyncAccedoOneUserImpl(this);
    }
}