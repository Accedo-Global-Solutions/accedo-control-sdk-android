package tv.accedo.one.sdk.definition;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOneCache {
    /**
     * @param context
     * @param key the metadata to fetch
     * @return the metadata requested by the key, or null if not yet fetched or empty.
     */
    String getMetadata(Context context, String key);

    /**
     * @param context
     * @return the list of all metadatas, or null if not yet fetched or empty.
     */
    Map<String, String> getAllMetadata(Context context);

    /**
     * @param context
     * @return the list of all metadatas as raw json, or null if not yet fetched or empty.
     */
    JSONObject getAllMetadataRaw(Context context);

    /**
     * @param context
     * @param key the asset to fetch
     * @return the asset requested by the key, or null if not yet fetched or empty.
     */
    byte[] getAsset(Context context, String key);

    /**
     * @param context
     * @return the list of all asset URLS, or null if not yet fetched or empty.
     */
    Map<String, String> getAllAssets(Context context);

    /**
     * @param context
     * @return The downloaded asset urls, and their content, or null if nothing has been fetched yet.
     */
    Map<String, byte[]> getAllAssetsRaw(Context context);

    /**
     * Removes all content from the cache.
     * @param context
     */
    void clear(Context context);
}