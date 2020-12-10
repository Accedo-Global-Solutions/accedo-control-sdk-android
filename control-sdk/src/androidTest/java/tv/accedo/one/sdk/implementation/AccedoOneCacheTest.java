/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Log;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Locale;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.utils.InternalStorage;
import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.model.AccedoOneException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotEquals;
import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;
import static tv.accedo.one.sdk.implementation.Constants.PATH_METADATA;

@RunWith(AndroidJUnit4.class)
public class AccedoOneCacheTest {
    private final AccedoOne accedoOne = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID);
    private final AccedoOne accedoOneGid = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID).setGid("TESTGID");

    @Test
    public void testServerTimeLocale() {
        try {
            Locale.setDefault(Locale.GERMANY);
            Response.DATE_HEADER_FORMAT.parse("Thu, 05 Dec 2019 13:42:08 GMT"); // Copied out from an actual date header.
        } catch (ParseException e) {
            fail(Log.getStackTraceString(e));
        }
    }

    @Test
    public void testIfModifiedSince() {
        try {
            final String mockJson = new JSONObject().put("TEST", "TEST").toString();
            final Context context = getContext();

            // Clear cache
            accedoOne.cache().clear(context);

            // Fetch so we'd have the lastest version
            accedoOne.control().getAllMetadataRaw(context);
            assertNotNull(accedoOne.cache().getAllMetadataRaw(context));

            // Modify cache contents, so we'd know we're getting cache data
            String cacheKey = IfModifiedTask.getCacheKey(getUrl(PATH_METADATA), accedoOne.getAppKey(), accedoOne.getGid());
            InternalStorage.write(context, mockJson.getBytes(), cacheKey);

            // Do another fetch, and see if we got the cached one.
            JSONObject response = accedoOne.control().getAllMetadataRaw(context);
            assertEquals(response.toString(), mockJson);

            // Modify cache timestamp, to force a modified response.
            String timestampKey = IfModifiedTask.getTimestampCacheKey(getUrl(PATH_METADATA), accedoOne.getAppKey(), accedoOne.getGid());
            InternalStorage.write(context, IfModifiedTask.sdfIfModifiedSince.parse("Fri, 18 Dec 1987 08:28:47 GMT").getTime(), timestampKey);
            // NOTE: if you comment this section, and instead put a breakpoint here, and modify AccedoOne control manually, then resume, the test should also succeed.

            // Do another fetch, and see if we got a fresh response
            JSONObject responseModified = accedoOne.control().getAllMetadataRaw(context);
            assertNotEquals(responseModified.toString(), mockJson);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPrefetchMetadata() {
        try {
            accedoOne.control().getAllMetadata(getContext());
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPrefetchAssets() {
        try {
            accedoOne.control().getAllAssetsRaw(getContext());
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMetadata() {
        try {
            accedoOne.control().getAllMetadata(getContext());
            assertEquals(accedoOne.cache().getMetadata(getContext(), "test_metadata"), "Test test this is a test.");
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMetadataGid() {
        try {
            accedoOneGid.control().getAllMetadata(getContext());
            assertEquals(accedoOneGid.cache().getMetadata(getContext(), "test_metadata"), "Test test this is GID a test.");
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAsset() {
        try {
            accedoOne.control().getAllAssetsRaw(getContext());
            assertEquals(new String(accedoOne.cache().getAsset(getContext(), "test_asset")), "Test test this is a test.\n");
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testClear() {
        try {
            accedoOne.control().getAllMetadata(getContext());
            assertNotNull(accedoOne.cache().getMetadata(getContext(), "test_metadata"));

            accedoOne.cache().clear(getContext());
            assertNull(accedoOne.cache().getMetadata(getContext(), "test_metadata"));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
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