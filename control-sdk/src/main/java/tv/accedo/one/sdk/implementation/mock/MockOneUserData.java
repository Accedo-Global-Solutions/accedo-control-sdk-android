package tv.accedo.one.sdk.implementation.mock;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOneUserData;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOneUser;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOneUserImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.implementation.utils.InternalStorage;
import tv.accedo.one.sdk.implementation.utils.Utils;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class MockOneUserData implements AccedoOneUserData {
    private static final String FILE_USERDATA = "USERDATA";

    @NonNull
    @Override
    public Map<String, String> getAllUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId) throws AccedoOneException {
        Map<String, String> userData = (Map<String, String>) InternalStorage.read(context, getFileName(scope, userId));
        if(userData==null){
            userData = new HashMap<>();
        }
        return userData;
    }

    @Override
    public void setAllUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId, Map<String, String> userData) throws AccedoOneException {
        if (userData == null) {
            InternalStorage.delete(context, getFileName(scope, userId));
        } else {
            InternalStorage.write(context, userData, getFileName(scope, userId));
        }
    }

    @NonNull
    @Override
    public String getUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId, @NonNull String key) throws AccedoOneException {
        return getAllUserData(context, scope, userId).get(key);
    }

    @Override
    public void setUserData(@NonNull Context context, @NonNull Scope scope, @NonNull String userId, @NonNull String key, @NonNull String value) throws AccedoOneException {
        Map<String, String> userData = getAllUserData(context, scope, userId);
        userData.put(key, value);
        setAllUserData(context, scope, userId, userData);
    }

    private String getFileName(Scope scope, String userId){
        return Utils.md5Hash(FILE_USERDATA+scope.name()+userId);
    }

    @NonNull
    @Override
    public AsyncAccedoOneUser async() {
        return new AsyncAccedoOneUserImpl(this);
    }
}
