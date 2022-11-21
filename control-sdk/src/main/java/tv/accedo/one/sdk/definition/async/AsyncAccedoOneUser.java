/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.definition.async;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import tv.accedo.one.sdk.definition.AccedoOneUserData.Scope;
import tv.accedo.one.sdk.model.AccedoOneException;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AsyncAccedoOneUser {
    /**
     * @param context
     * @param scope     either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param onSuccess Returns a map of all the stored user data
     * @param onFailure Returns information about the error the happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getAllUserData(@NonNull Context context, Scope scope, @NonNull String userId, @Nullable Callback<Map<String, String>> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * Replaces the currently stored userData with the given key-value map.
     * <p>
     * Caution: This method does not simply replace conflicting keys. All user data will be replaced
     * by the information provided in the request - if the request does not contain a key - data saved for that key will be lost.
     *
     * @param context
     * @param scope     either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param userData
     * @param onSuccess Called if the operation was successful
     * @param onFailure Returns information about the error the happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable setAllUserData(@NonNull Context context, Scope scope, @NonNull String userId, Map<String, String> userData, @Nullable Callback<Void> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * @param context
     * @param scope     either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param key       cannot be null
     * @param onSuccess Called if the operation was successful
     * @param onFailure Returns information about the error the happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable getUserData(@NonNull Context context, Scope scope, String userId, String key, @Nullable Callback<String> onSuccess, @Nullable Callback<AccedoOneException> onFailure);

    /**
     * Set data for the user for a specified key. If the given key already exists, it will be overwritten.
     *
     * @param context
     * @param scope     either APPLICATION or APPLICATION_GROUP
     * @param userId
     * @param key       cannot be null
     * @param value     can be null, in that case it will delete the existing stored data
     * @param onSuccess Called if the operation was successful
     * @param onFailure Returns information about the error the happened
     * @return the AsyncTask used for threading by the call
     */
    Cancellable setUserData(@NonNull Context context, Scope scope, String userId, String key, String value, @Nullable Callback<Void> onSuccess, @Nullable Callback<AccedoOneException> onFailure);
}
