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
import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.implementation.mock.ConfigSource;
import tv.accedo.one.sdk.implementation.mock.MockOne;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class MockOnePublishByAliasTest {
    public static final String TYPE_ALIAS = "test-type-1";
    public static final String ENTRY_ALIAS_1 = "test-entry-1";
    public static final String ENTRY_ALIAS_LOCALISED = "test-entry-localised";
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_AR = "ar";

    private final AccedoOne accedoOne = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID);

    private final AccedoOne mockOne = new MockOne(new ConfigSource() {
        @Override
        public JSONObject getAllMetadata(Context context) throws Exception {
            return accedoOne.control().getAllMetadataRaw(context);
        }

        @Override
        public Map<String, String> getAllAssets(Context context) throws Exception {
            return accedoOne.control().getAllAssets(context);
        }

        @Override
        public JSONArray getAllEntries(Context context) throws Exception {
            return accedoOne.publish().getAllEntries(context);
        }
    });

    @Test
    public void testEntry(){
        try {
            assertNotNull(mockOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_1));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntries(){
        try {
            JSONArray jsonArray = mockOne.publish().byAlias().getEntries(getContext(), Arrays.asList(ENTRY_ALIAS_1, ENTRY_ALIAS_LOCALISED));

            assertNotNull(jsonArray);
            assertFalse(jsonArray.length() == 0);
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntriesByTypeAlias(){
        try {
            assertNotNull(mockOne.publish().byAlias().getEntries(getContext(), TYPE_ALIAS));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllEntriesWithParameters(){
        try {
            PagedResponse pagedResponse = mockOne.publish().byAlias().getEntries(getContext(), TYPE_ALIAS, new PaginatedParams().setOffset(0).setSize(2));
            assertTrue(pagedResponse != null && pagedResponse.getOffset() == 0 && pagedResponse.getEntries().length() == 2 && pagedResponse.getSize() == 2);
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testNullEntry(){
        try {
            mockOne.publish().byAlias().getEntry(getContext(), null);
            fail();
        } catch (AccedoOneException e) {
            e.printStackTrace();
        }
    }
}
