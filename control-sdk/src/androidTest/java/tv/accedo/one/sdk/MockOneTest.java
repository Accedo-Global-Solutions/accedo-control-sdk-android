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

import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.implementation.mock.ConfigSource;
import tv.accedo.one.sdk.implementation.mock.MockOne;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.ApplicationStatus.Status;
import tv.accedo.one.sdk.implementation.parsers.JSONMapByteParser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class MockOneTest {
    private final AccedoOne accedoOneHelper = new AccedoOneImpl(Shared.API_URL, Shared.APP_ID, Shared.DEVICE_ID);

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
    public void testAssets(){
        try {
            accedoOne.control().getAllAssets(Shared.getContext());
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMetadata(){
        try {
            accedoOne.control().getAllMetadata(Shared.getContext());
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testApplicationStatus(){
        assertEquals(accedoOne.control().getApplicationStatus(Shared.getContext()).getStatus(), Status.ACTIVE);
    }

    //TODO
//    @Test
//    public void testLog(){
//        final long timestamp = System.currentTimeMillis();
//
//        LogListener logListener = new LogListener() {
//            @Override
//            public void onLog(String tag, int priority, Throwable ex, String message, Object... args) {
//                assertTrue(new LogEntry(LogLevel.debug, timestamp, 1, "Test", "Android", "Test").toString().equalsIgnoreCase(message));
//            }
//        };
//
//        L.addLogListener(logListener);
//        accedoOnec.logging().log(LogLevel.debug, 1, "Test", "Android", "Test");
//        L.removeLogListener(logListener);
//    }

    @Test
    public void testAllMetadataRaw() {
        try {
            JSONObject jsonObject = accedoOne.control().getAllMetadataRaw(Shared.getContext());
            Map<String, String> map = accedoOne.control().getAllMetadata(Shared.getContext());

            Map<String, String> revertedMap = new JSONMapByteParser().parse(jsonObject.toString().getBytes());

            assertTrue(map.equals(revertedMap));

        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }
}
