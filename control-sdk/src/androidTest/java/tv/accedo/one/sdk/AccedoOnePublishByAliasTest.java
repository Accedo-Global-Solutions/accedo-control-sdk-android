/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;


import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class AccedoOnePublishByAliasTest {
    public static final String TYPE_ALIAS = "test-type-1";
    public static final String ENTRY_ALIAS_1 = "test-entry-1";
    public static final String ENTRY_ALIAS_LOCALISED = "test-entry-localised";
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_AR = "ar";

    private final AccedoOne accedoOne = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID);

    @Test
    public void testEntry(){
        try {
            assertNotNull(accedoOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_1));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntries(){
        try {
            assertNotNull(accedoOne.publish().byAlias().getEntries(getContext(), Arrays.asList(ENTRY_ALIAS_1, ENTRY_ALIAS_LOCALISED)));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntriesByTypeAlias(){
        try {
            assertNotNull(accedoOne.publish().byAlias().getEntries(getContext(), TYPE_ALIAS));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntryWrongParameters(){
        try {
            accedoOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_1, new OptionalParams().setPreview(true).setAt(new Date()));
            fail();
        } catch (AccedoOneException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEntryCorrectParameters(){
        try {
            assertNotNull(accedoOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_1, new OptionalParams().setPreview(true)));
            assertNotNull(accedoOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_1, new OptionalParams().setAt(new Date(accedoOne.getServerTime()))));
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllEntriesWithParameters(){
        try {
            PagedResponse pagedResponse = accedoOne.publish().byAlias().getEntries(getContext(), TYPE_ALIAS, new PaginatedParams().setOffset(0).setSize(2));
            assertTrue(pagedResponse != null && pagedResponse.getOffset() == 0 && pagedResponse.getEntries().length() == 2 && pagedResponse.getSize() == 2);
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEntriesAtOldDate(){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1987, 12, 18);

            JSONArray jsonArray = accedoOne.publish().byAlias().getEntries(
                    getContext(),
                    Arrays.asList(ENTRY_ALIAS_1, ENTRY_ALIAS_LOCALISED),
                    new OptionalParams().setAt(calendar.getTime())
            );
            assertTrue(jsonArray!=null && jsonArray.length()==0);
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testNullEntry(){
        try {
            accedoOne.publish().byAlias().getEntry(getContext(), null);
            fail();
        } catch (AccedoOneException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLocalisation(){
        try {
            assertEquals(accedoOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_LOCALISED, new OptionalParams(LOCALE_EN)).getString("description"), "Test");
            assertEquals(accedoOne.publish().byAlias().getEntry(getContext(), ENTRY_ALIAS_LOCALISED, new OptionalParams(LOCALE_AR)).getString("description"), "اختبار");
        } catch (AccedoOneException | JSONException e) {
            e.printStackTrace();
            fail();
        }
    }
}
