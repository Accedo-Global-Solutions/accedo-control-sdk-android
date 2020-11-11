/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import tv.accedo.one.sdk.definition.AccedoOneCache;
import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.utils.InternalStorage;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.implementation.parsers.JSONMapByteParser;
import tv.accedo.one.sdk.implementation.parsers.JSONObjectByteParser;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneCacheImpl extends Constants implements AccedoOneCache {
    private AccedoOne accedoOne;

    public AccedoOneCacheImpl(AccedoOne accedoOne) {
        this.accedoOne = accedoOne;
    }

    @Override
    public String getMetadata(Context context, String key) {
        Map<String, String> allMetadata = getAllMetadata(context);
        if (allMetadata == null) {
            return null;
        }

        return allMetadata.get(key);
    }

    @Override
    public Map<String, String> getAllMetadata(Context context) {
        String cacheKey = IfModifiedTask.getCacheKey(getUrl(PATH_METADATA), accedoOne.getAppKey(), accedoOne.getGid());

        return getMapFromCache(context, cacheKey);
    }

    @Override
    public JSONObject getAllMetadataRaw(Context context) {
        String cacheKey = IfModifiedTask.getCacheKey(getUrl(PATH_METADATA), accedoOne.getAppKey(), accedoOne.getGid());

        return getJSONFromCache(context, cacheKey);
    }

    @Override
    public byte[] getAsset(Context context, String key) {
        Map<String, String> allAssets = getAllAssets(context);
        if (allAssets == null) {
            return null;
        }

        String assetUrl = allAssets.get(key);
        if (TextUtils.isEmpty(assetUrl)) {
            return null;
        }

        String cacheKey = IfModifiedTask.getCacheKey(assetUrl, accedoOne.getAppKey(), accedoOne.getGid());
        return (byte[]) InternalStorage.read(context, cacheKey);
    }

    @Override
    public Map<String, String> getAllAssets(Context context) {
        String cacheKey = IfModifiedTask.getCacheKey(getUrl(PATH_ASSETS), accedoOne.getAppKey(), accedoOne.getGid());

        return getMapFromCache(context, cacheKey);
    }

    @Override
    public Map<String, byte[]> getAllAssetsRaw(Context context) {
        HashMap<String, byte[]> result = new HashMap<>();

        Map<String, String> assets = getAllAssets(context);
        if (assets == null) {
            return null;
        }

        for (Entry<String, String> entry : assets.entrySet()) {
            byte[] asset = getAsset(context, entry.getKey());
            if (asset != null) {
                result.put(entry.getKey(), asset);
            }
        }

        return result.isEmpty() ? null : result;
    }

    @Override
    public void clear(Context context) {
        IfModifiedTask.cleanup(context, true);
    }

    private Map<String, String> getMapFromCache(Context context, String cacheKey) {
        try {
            byte[] cachedValue = (byte[]) InternalStorage.read(context, cacheKey);
            return new JSONMapByteParser().parse(cachedValue);
        } catch (AccedoOneException e) {
            return null;
        }
    }

    private JSONObject getJSONFromCache(Context context, String cacheKey) {
        try {
            byte[] cachedValue = (byte[]) InternalStorage.read(context, cacheKey);
            return new JSONObjectByteParser().parse(cachedValue);
        } catch (AccedoOneException e) {
            return null;
        }
    }

    private String getUrl(String path) {
        Uri uri = Uri.parse(accedoOne.getEndpoint() + path);
        if (!TextUtils.isEmpty(accedoOne.getGid())) {
            uri = uri.buildUpon().appendQueryParameter("gid", accedoOne.getGid()).build();
        }
        return uri.toString();
    }
}