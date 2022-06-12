package tv.accedo.one.sdk.definition;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOneCache {
    /**
     * @param context
     * @param key     the metadata to fetch
     * @return the metadata requested by the key, or null if not yet fetched or empty.
     */
    @Nullable
    String getMetadata(@NonNull Context context, @NonNull String key);

    /**
     * @param context
     * @return the list of all metadatas, or null if not yet fetched or empty.
     */
    @Nullable
    Map<String, String> getAllMetadata(@NonNull Context context);

    /**
     * @param context
     * @return the list of all metadatas as raw json, or null if not yet fetched or empty.
     */
    @Nullable
    JSONObject getAllMetadataRaw(@NonNull Context context);

    /**
     * @param context
     * @param key     the asset to fetch
     * @return the asset requested by the key, or null if not yet fetched or empty.
     */
    @Nullable
    byte[] getAsset(@NonNull Context context, @NonNull String key);

    /**
     * @param context
     * @return the list of all asset URLS, or null if not yet fetched or empty.
     */
    @Nullable
    Map<String, String> getAllAssets(@NonNull Context context);

    /**
     * @param context
     * @return The downloaded asset urls, and their content, or null if nothing has been fetched yet.
     */
    @Nullable
    Map<String, byte[]> getAllAssetsRaw(@NonNull Context context);

    /**
     * Removes all content from the cache.
     *
     * @param context
     */
    void clear(@NonNull Context context);
}
