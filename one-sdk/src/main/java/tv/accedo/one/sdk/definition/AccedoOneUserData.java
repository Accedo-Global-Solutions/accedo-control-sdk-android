package tv.accedo.one.sdk.definition;

import android.content.Context;

import java.util.Map;

import tv.accedo.one.sdk.definition.async.AsyncAccedoOneUser;
import tv.accedo.one.sdk.model.AccedoOneException;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOneUserData {
    public static enum Scope {
        APPLICATION, APPLICATION_GROUP
    }

    /**
     * @param context
     * @param scope either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @return a map of all the stored user data
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    Map<String, String> getAllUserData(Context context, Scope scope, String userId) throws AccedoOneException;

    /**
     * Replaces the currently stored userData with the given key-value map.
     *
     * Caution: This method does not simply replace conflicting keys. All user data will be replaced
     * by the information provided in the request - if the request does not contain a key - data saved for that key will be lost.
     *
     * @param context
     * @param scope either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param userData
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    void setAllUserData(Context context, Scope scope, String userId, Map<String, String> userData) throws AccedoOneException;

    /**
     * @param context
     * @param scope either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param key cannot be null
     * @return The userdata associated with the given key
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    String getUserData(Context context, Scope scope, String userId, String key) throws AccedoOneException;

    /**
     * Set data for the user for a specified key. If the given key already exists, it will be overwritten.
     * The value cannot be null, so this call cannot be used for deleting. See setAllUserData() for that.
     *
     * @param context
     * @param scope either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param key cannot be null
     * @param value cannot be null
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    void setUserData(Context context, Scope scope, String userId, String key, String value) throws AccedoOneException;

    /**
     * Used for connecting asynchronously, using callbacks.
     * @return an {@link AsyncAccedoOneUser} instance, containing this {@link AccedoOneUserData} instance
     */
    AsyncAccedoOneUser async();
}