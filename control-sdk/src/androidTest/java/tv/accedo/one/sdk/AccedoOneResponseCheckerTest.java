/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.implementation.AccedoOneImpl;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;

import static tv.accedo.one.sdk.Shared.API_URL;
import static tv.accedo.one.sdk.Shared.APP_ID;
import static tv.accedo.one.sdk.Shared.DEVICE_ID;
import static tv.accedo.one.sdk.Shared.getContext;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AccedoOneResponseCheckerTest {
    private final AccedoOne accedoOne = new AccedoOneImpl(API_URL, APP_ID, DEVICE_ID);

    @Test
    public void testMissingPublishEntry() {
        try {
            accedoOne.publish().getEntry(getContext(), "YOU WONT FIND THIS");
        } catch (AccedoOneException e) {
            assertTrue(StatusCode.INVALID_PARAMETERS.equals(e.statusCode)); //Publish throws 400 bad request
        }
    }

    @Test
    public void testMissingPublishEntryType() {
        try {
            accedoOne.publish().getEntries(getContext(), "YOU WONT FIND THIS");
        } catch (AccedoOneException e) {
            assertTrue(StatusCode.INVALID_PARAMETERS.equals(e.statusCode)); //Publish throws 400 bad request
        }
    }

    @Test
    public void testMissingMetadata() {
        try {
            accedoOne.control().getMetadata(getContext(), "YOU WONT FIND THIS");
        } catch (AccedoOneException e) {
            assertTrue(StatusCode.KEY_NOT_FOUND.equals(e.statusCode)); //Metadata throws 404 not found
        }
    }

    @Test
    public void testMissingAsset() {
        try {
            accedoOne.control().getAsset(getContext(), "YOU WONT FIND THIS");
        } catch (AccedoOneException e) {
            assertTrue(StatusCode.KEY_NOT_FOUND.equals(e.statusCode)); //Asset throws KEY_NOT_FOUND the same as metadata
        }
    }
}