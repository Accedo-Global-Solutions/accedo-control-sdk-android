package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public Map<String, String> getAllUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || scope == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Call
        return accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId)
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker())
                .getParsedText(new JSONMapParser());
    }

    @Override
    public void setAllUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId, Map<String, String> userData) throws AccedoOneException {
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
                .setMethod(Method.POST, jsonObject.toString())
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker());
    }

    @NonNull
    @Override
    public String getUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId, @NonNull String key) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || scope == null || key == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Call
        return accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId + "/" + key)
                .connect(accedoOneImpl.okHttpClient ,new AccedoOneResponseChecker())
                .getText();
    }

    @Override
    public void setUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId, @NonNull String key, @NonNull String value) throws AccedoOneException {
        //Input param check
        if (TextUtils.isEmpty(userId) || key == null || value == null || scope == null) {
            throw new AccedoOneException(StatusCode.INVALID_PARAMETERS);
        }

        //Call
        accedoOneImpl.createSessionedRestClient(accedoOneImpl.getEndpoint() + getPathForScope(scope) + userId + "/" + key)
                .setMethod(Method.POST, value)
                .setHeader("Content-Type", "text/plain; charset=utf-8")
                .connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker());
    }

    private String getPathForScope(Scope scope){
        return Scope.APPLICATION.equals(scope)? PATH_USER_APPLICATION_DATA : PATH_USER_APPLICATIONGROUP_DATA;
    }

    @NonNull
    @Override
    public AsyncAccedoOneUser async() {
        return new AsyncAccedoOneUserImpl(this);
    }
}
