package tv.accedo.one.sdk.implementation.mock;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface ConfigSource {
    /**
     * @param context
     * @return a JSONObject containing key-value pairs that represent all Accedo One metadata
     * @throws Exception if for whatever reason the ConfigSource can't provide data, it should throw an exception
     */
    public JSONObject getAllMetadata(Context context) throws Exception;

    /**
     * Returns a map of key-url pair representing Accedo One assets. Files in the Android assets folder should be represented as file:///android_asset/myfile
     *
     * @param context
     * @return a JSONObject containing key-value pairs that represent Accedo One asset URL-s
     * @throws Exception if for whatever reason the ConfigSource can't provide data, it should throw an exception
     */
    public Map<String, String> getAllAssets(Context context) throws Exception;

    /**
     * @param context
     * @return a JSONArray containing a list of JSONObjects that represent all Accedo One Publish entries
     * @throws Exception if for whatever reason the ConfigSource can't provide data, it should throw an exception
     */
    public JSONArray getAllEntries(Context context) throws Exception;
}