/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.definition.AccedoOneUserData.Scope;
import tv.accedo.one.sdk.implementation.mock.MockOne;
import tv.accedo.one.sdk.model.AccedoOneException;

import static tv.accedo.one.sdk.Shared.getContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class MockOneUserTest {
    private final AccedoOne accedoOne = new MockOne(null, null, null);

    @Test
    public void testAllUserData(){
        try {
            //PUT
            HashMap<String, String> requestMap = new HashMap<String, String>();
            requestMap.put("username", "a1");
            requestMap.put("lastname", "Béla");
            accedoOne.userData().setAllUserData(getContext(), Scope.APPLICATION, "a1", requestMap);

            //GET
            Map<String, String> responseMap = accedoOne.userData().getAllUserData(getContext(), Scope.APPLICATION, "a1");

            //ASSERT
            assertEquals(requestMap, responseMap);

        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUserData(){
        try {
            //INIT
            HashMap<String, String> requestMap = new HashMap<String, String>();
            requestMap.put("username", "a1");
            requestMap.put("lastname", "Béla");
            accedoOne.userData().setAllUserData(getContext(), Scope.APPLICATION, "a1", requestMap);

            //PUT
            accedoOne.userData().setUserData(getContext(), Scope.APPLICATION, "a1", "lastname", "Józsi");

            //GET
            String newLastname = accedoOne.userData().getUserData(getContext(), Scope.APPLICATION, "a1", "lastname");

            //ASSERT
            assertEquals("Józsi", newLastname);

        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteAllUserData(){
        try {
            //INIT
            HashMap<String, String> requestMap = new HashMap<String, String>();
            requestMap.put("username", "a1");
            requestMap.put("lastname", "Béla");
            accedoOne.userData().setAllUserData(getContext(), Scope.APPLICATION, "a1", requestMap);

            //DELETE
            accedoOne.userData().setAllUserData(getContext(), Scope.APPLICATION, "a1", null);

            //GET
            Map<String, String> result = accedoOne.userData().getAllUserData(getContext(), Scope.APPLICATION, "a1");

            //ASSERT
            assertTrue(result.isEmpty());

        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDifferentUserData(){
        try {
            //PUT a1
            HashMap<String, String> requestMap1 = new HashMap<String, String>();
            requestMap1.put("username", "a1");
            requestMap1.put("lastname", "Béla");
            accedoOne.userData().setAllUserData(getContext(), Scope.APPLICATION, "a1", requestMap1);

            //PUT a2
            HashMap<String, String> requestMap2 = new HashMap<String, String>();
            requestMap2.put("username", "a2");
            requestMap2.put("lastname", "Józsi");
            accedoOne.userData().setAllUserData(getContext(), Scope.APPLICATION, "a2", requestMap2);

            //GET
            Map<String, String> responseMap1 = accedoOne.userData().getAllUserData(getContext(), Scope.APPLICATION, "a1");
            Map<String, String> responseMap2 = accedoOne.userData().getAllUserData(getContext(), Scope.APPLICATION, "a2");

            //ASSERT
            assertTrue(!responseMap1.equals(responseMap2));
            assertEquals(requestMap1, responseMap1);
            assertEquals(requestMap2, responseMap2);

        } catch (AccedoOneException e) {
            e.printStackTrace();
            fail();
        }
    }
}
