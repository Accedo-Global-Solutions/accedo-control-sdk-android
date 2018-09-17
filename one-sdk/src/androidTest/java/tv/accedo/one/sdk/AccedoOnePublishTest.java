/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;

import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.PublishLocale;
import tv.accedo.one.sdk.model.LogEntry;
import tv.accedo.one.sdk.model.LogLevel;
import tv.accedo.one.sdk.model.OptionalParams;
import tv.accedo.one.sdk.model.PagedResponse;
import tv.accedo.one.sdk.model.PaginatedParams;

import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class AccedoOnePublishTest {
    public static final String TYPE_ID = "592d68e9a0e845000622704b";
    public static final String ENTRY_ID_1 = "592d699ca0e8450006227053";
    public static final String ENTRY_ID_LOCALISED = "592d69cba0e8450006227059";
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_AR = "ar";

    private final AccedoOne accedoOne = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID);

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
            assertNotNull(accedoOne.publish().getEntries(getContext(), Arrays.asList(ENTRY_ID_1, ENTRY_ID_LOCALISED)));
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
    public void testEntryWrongParameters(){
        try {
            accedoOne.publish().getEntry(getContext(), ENTRY_ID_1, new OptionalParams().setPreview(true).setAt(new Date()));
            fail();
        } catch (AccedoOneException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEntryCorrectParameters(){
        try {
            assertNotNull(accedoOne.publish().getEntry(getContext(), ENTRY_ID_1, new OptionalParams().setPreview(true)));
            assertNotNull(accedoOne.publish().getEntry(getContext(), ENTRY_ID_1, new OptionalParams().setAt(new Date(accedoOne.getServerTime()))));
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
    public void testEntriesAtOldDate(){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1987, 12, 18);

            JSONArray jsonArray = accedoOne.publish().getEntries(
                    getContext(),
                    Arrays.asList(ENTRY_ID_1, ENTRY_ID_LOCALISED),
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
            accedoOne.publish().getEntry(getContext(), null);
            fail();
        } catch (AccedoOneException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLocalisation(){
        try {
            assertEquals(accedoOne.publish().getEntry(getContext(), ENTRY_ID_LOCALISED, new OptionalParams(LOCALE_EN)).getString("description"), "Test");
            assertEquals(accedoOne.publish().getEntry(getContext(), ENTRY_ID_LOCALISED, new OptionalParams(LOCALE_AR)).getString("description"), "اختبار");
        } catch (AccedoOneException | JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAvailableLocales(){
        try {
            List<PublishLocale> publishLocaleList = accedoOne.publish().getAvailableLocales(getContext());

            assertTrue(publishLocaleList.size() == 2);
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLogEntryEquals(){
        long timestamp = System.currentTimeMillis();

        LogEntry logEntry1 = new LogEntry(LogLevel.debug, timestamp, 1, "message", "dim1", "dim2");
        LogEntry logEntry2 = new LogEntry(LogLevel.debug, timestamp, 1, "message", "dim1", "dim2");
        LogEntry logEntry3 = new LogEntry(LogLevel.debug, timestamp, 1, "message", "dim1", "dim3");
        LogEntry logEntry4 = new LogEntry(LogLevel.debug, timestamp, 1, "message", "dim1", "dim2", "dim3");

        assertTrue(logEntry1.equals(logEntry2));
        assertFalse(logEntry2.equals(logEntry3));
        assertFalse(logEntry3.equals(logEntry4));
        assertFalse(logEntry1.equals(logEntry4));
    }
}