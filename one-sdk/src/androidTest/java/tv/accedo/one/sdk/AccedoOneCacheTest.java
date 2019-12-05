/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Locale;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.model.AccedoOneException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;

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
}