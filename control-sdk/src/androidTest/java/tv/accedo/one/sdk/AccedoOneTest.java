/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.UnknownHostException;
import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.ApplicationStatus.Status;
import tv.accedo.one.sdk.model.Profile;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class AccedoOneTest {
    private final AccedoOne accedoOne = new AccedoOneImpl(Shared.API_URL, Shared.APP_ID, Shared.DEVICE_ID).setLoggingPeriod(300);
    private final AccedoOne accedoOneGid = new AccedoOneImpl(Shared.API_URL, Shared.APP_ID, Shared.DEVICE_ID).setGid("TESTGID");
    private final AccedoOne accedoOneInvalidGid = new AccedoOneImpl(Shared.API_URL, Shared.APP_ID, Shared.DEVICE_ID).setGid("TESTINVALIDGID");
    private final AccedoOne accedoOneFailing = new AccedoOneImpl("http://www.notappgrid.com", Shared.APP_ID, Shared.DEVICE_ID);

    //TODO
//    @Test
//    public void testAnalytics(){
//        LogListener logListener = new LogListener() {
//            @Override
//            public void onLog(String tag, int priority, Throwable ex, String message, Object... args) {
//                assertFalse(priority == Log.WARN || priority == Log.ERROR);
//            }
//        };
//
//        L.addLogListener(logListener);
//        accedoOne.logging().applicationStart();
//        accedoOne.logging().applicationQuit();
//        L.removeLogListener(logListener);
//    }

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
//        final ConditionVariable conditionVariable = new ConditionVariable(false);
//
//        LogListener logListener = new LogListener() {
//            @Override
//            public void onLog(String tag, int priority, Throwable ex, String message, Object... args) {
//                if (priority == Log.WARN && "AppGrid".equalsIgnoreCase(tag)) {
//                    fail();
//                    conditionVariable.open();
//                } else if ("RestClient".equalsIgnoreCase(tag) && message.contains("\"successful\":2")) {
//                    conditionVariable.open();
//                } else if ("RestClient".equalsIgnoreCase(tag) && message.contains("\"failed\":2")) {
//                    fail();
//                    conditionVariable.open();
//                }
//            }
//        };
//
//        L.addLogListener(logListener);
//        accedoOne.logging().log(LogLevel.debug, 1, "Test");
//        accedoOne.logging().log(LogLevel.debug, 1, "Test");
//        conditionVariable.block(10000);
//        L.removeLogListener(logListener);
//    }

    @Test
    public void testValidGid(){
        try{
            String testMetadata = accedoOne.control().getMetadata(Shared.getContext(), "test_metadata");
            String testMetadataGID = accedoOneGid.control().getMetadata(Shared.getContext(), "test_metadata");

            assertTrue(!testMetadata.equals(testMetadataGID));

        }catch (AccedoOneException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testInvalidGid() {
        try{
            String testMetadata = accedoOne.control().getMetadata(Shared.getContext(), "test_metadata");
            String testMetadataNotGID = accedoOneInvalidGid.control().getMetadata(Shared.getContext(), "test_metadata");

            assertTrue(testMetadata.equals(testMetadataNotGID));

        }catch (AccedoOneException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllAssetsRaw() {
        try {
            Map<String, byte[]> allAssets = accedoOne.control().getAllAssetsRaw(Shared.getContext());
            assertFalse(allAssets.isEmpty());
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testProfile() {
        try {
            Profile profile = accedoOne.control().getProfile(Shared.getContext());
            assertNotNull(profile);
            assertNotNull(profile.getProfileId());
        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUnknownHostException() {
        try {
            accedoOneFailing.control().getProfile(Shared.getContext());
            fail();
        } catch (AccedoOneException e) {
            assertTrue(e.getCause() != null && e.getCause() instanceof UnknownHostException);
        }
    }

    @Test
    public void testPublishUnknownHostException() {
        try {
            accedoOneFailing.publish().getAllEntries(Shared.getContext());
            fail();
        } catch (AccedoOneException e) {
            assertTrue(e.getCause() != null && e.getCause() instanceof UnknownHostException);
        }
    }

    @Test
    public void testCachedUnknownHostException() {
        try {
            accedoOneFailing.control().getAllMetadata(Shared.getContext());
            fail();
        } catch (AccedoOneException e) {
            assertTrue(e.getCause() != null && e.getCause() instanceof UnknownHostException);
        }
    }
}
