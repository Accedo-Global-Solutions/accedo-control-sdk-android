/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.implementation.mock.ConfigSource;
import tv.accedo.one.sdk.implementation.mock.MockOne;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class MockOnePublishTest {
    public static final String TYPE_ID = "592d68e9a0e845000622704b";
    public static final String ENTRY_ID_1 = "592d699ca0e8450006227053";
    public static final String ENTRY_ID_2 = "592d69cba0e8450006227059";

    private final AccedoOne accedoOneHelper = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID);

    private final AccedoOne accedoOne = new MockOne(new ConfigSource() {
        @Override
        public JSONObject getAllMetadata(Context context) throws Exception {
            return accedoOneHelper.control().getAllMetadataRaw(context);
        }

        @Override
        public JSONArray getAllEntries(Context context) throws Exception {
            return accedoOneHelper.publish().getAllEntries(context);
        }

        @Override
        public Map<String, String> getAllAssets(Context context) throws Exception {
            return accedoOneHelper.control().getAllAssets(context);
        }
    });

    @Test
    public void testEntry(){
        try {
            assertNotNull(accedoOne.publish().getEntry(getContext(), ENTRY_ID_1));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntries(){
        try {
            assertNotNull(accedoOne.publish().getEntries(getContext(), Arrays.asList(ENTRY_ID_1, ENTRY_ID_2)));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntriesByType(){
        try {
            assertNotNull(accedoOne.publish().getEntries(getContext(), TYPE_ID));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllEntries(){
        try {
            assertNotNull(accedoOne.publish().getAllEntries(getContext()));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntryCorrectParameters(){
        try {
            assertNotNull(accedoOne.publish().getEntry(getContext(), ENTRY_ID_1, new OptionalParams().setPreview(true)));
            assertNotNull(accedoOne.publish().getEntry(getContext(), ENTRY_ID_1, new OptionalParams().setAt(new Date())));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllEntriesWithParameters(){
        try {
            PagedResponse pagedResponse = accedoOne.publish().getAllEntries(getContext(), new PaginatedParams().setOffset(0).setSize(3));
            assertTrue(pagedResponse != null && pagedResponse.getOffset() == 0 && pagedResponse.getEntries().length() == 3 && pagedResponse.getSize() == 3);
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testNullEntry(){
        try {
            accedoOne.publish().getEntry(getContext(), null);
            fail();
        } catch (AccedoOneException e) {
            e.printStackTrace();
        }
    }
}
