package tv.accedo.one.sdk.definition;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.Map;

import tv.accedo.one.sdk.definition.async.AsyncAccedoOneControl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.ApplicationStatus;
import tv.accedo.one.sdk.model.Profile;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOneControl {
    /**
     * Returns the application status. If no connection was possible, this call will return ACTIVE, as per Accedo One's best effort maintenance mode policy.
     *
     * @return The application status and message.
     */
    @NonNull
    ApplicationStatus getApplicationStatus(@NonNull Context context);

    /**
     * @return information about which profile your device and application is matching right now. Also contains A/B testing info.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    Profile getProfile(@NonNull Context context) throws AccedoOneException;

    /**
     * Convenience method for calling getAllMetadata(context).get(key)
     * If the given key is not specified in the Accedo One config, the thrown exception will have a KEY_NOT_FOUND status.
     * 
     * The reason for calling getAllMetadata, and not the single key backend API is to optimize the offline mode cache.
     * 
     * @param context 
     * @param key The key to fetch.
     * @return The value associated with the given key.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    String getMetadata(@NonNull Context context, @NonNull String key) throws AccedoOneException;

    /**
     * @param context
     * @return All the values stored in the Accedo One configs, by key.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    Map<String, String> getAllMetadata(@NonNull Context context) throws AccedoOneException;

    /**
     * @param context
     * @return All the values stored in the Accedo One configs, as raw json.
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    JSONObject getAllMetadataRaw(@NonNull Context context) throws AccedoOneException;

    /**
     * Fetches the resource file specified by the key.
     *
     * @param context
     * @param key The resource key
     * @return The resource, as a byte array
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    byte[] getAsset(@NonNull Context context, @NonNull String key) throws AccedoOneException;
    
    /**
     * Fetches and stores all resource urls from the server.
     *
     * @param context
     * @return A map containing urls to all the resources
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    Map<String, String> getAllAssets(@NonNull Context context) throws AccedoOneException;

    /**
     * Fetches and stores assetUrls, and the urls' contents aswell.
     *
     * @param context
     * @return The downloaded asset urls, and their content
     * @throws AccedoOneException containing appropriate StatusCode on what happened
     */
    @NonNull
    Map<String, byte[]> getAllAssetsRaw(@NonNull Context context) throws AccedoOneException;

    /**
     * Used for connecting asynchronously, using callbacks.
     * @return an {@link AsyncAccedoOneControl} instance, containing this {@link AccedoOneControl} instance
     */
    @NonNull
    AsyncAccedoOneControl async();
}
