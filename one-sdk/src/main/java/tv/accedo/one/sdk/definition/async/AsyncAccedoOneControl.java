/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.definition.async;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.ApplicationStatus;
import tv.accedo.one.sdk.model.Profile;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AsyncAccedoOneControl {
    /**
     * Returns the application status. If no connection was possible, this call will return ACTIVE, as per Accedo One's best effort maintenance mode policy.
     *
     * @param context
     * @param onSuccess Returns the application status and message.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getApplicationStatus(Context context, Callback<ApplicationStatus> onResult);

    /**
     * @param context
     * @param onSuccess Returns information about which profile your device and application is matching right now. Also contains A/B testing info.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode of what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getProfile(Context context, Callback<Profile> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * This is a convenience call for calling getAllMetadata and getting your one value from there.
     * 
     * The reason for calling getAllMetadata, and not the single key backend API is to optimize the offline mode cache.
     * 
     * @param context 
     * @param key The key to fetch.
     * @param onSuccess Returns the value associated with the given key..
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode of what happened.
     * @return the AsyncTask used for threading by the call 
     */
    Cancellable getMetadata(Context context, String key, Callback<String> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param onSuccess Returns all the values stored in the Accedo One configs, by key.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode of what happened.
     * @return the AsyncTask used for threading by the call 
     */
    Cancellable getAllMetadata(Context context, Callback<Map<String, String>> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param onSuccess Returns all the values stored in the Accedo One configs, as raw json.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode of what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllMetadataRaw(Context context, Callback<JSONObject> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * Fetches the resource file specified by the key.
     *
     * @param context The context that requests the resource file
     * @param key The resource key 
     * @param onSuccess Returns the resource, as a byte array
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode of what happened.
     * @return the AsyncTask used for threading by the call 
     */
    Cancellable getAsset(Context context, String key, Callback<byte[]> onSuccess, Callback<AccedoOneException> onFailure);
    
    /**
     * Fetches and stores all resources from the server.
     *
     * @param context The context that requests all the resources
     * @param onSuccess Returns a map containing urls to all the resources.
     * @param onFailure Returns the {@link AccedoOneException} containing an appropriate StatusCode of what happened.
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllAssets(Context context, Callback<Map<String, String>> onSuccess, Callback<AccedoOneException> onFailure);

    /**
     * Fetches and stores assetUrls, and the urls' contents aswell.
     *
     * @param context
     * @param onSuccess returns the downloaded asset urls, and their content
     * @param onFailure Returns the {@link AccedoOneException} containing appropriate StatusCode on what happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllAssetsRaw(Context context, Callback<Map<String, byte[]>> onSuccess, Callback<AccedoOneException> onFailure);
}