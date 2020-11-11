/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation.parsers;

import org.json.JSONObject;

import java.util.Locale;

import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.ApplicationStatus;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class ApplicationStatusParser implements ThrowingParser<Response, ApplicationStatus, AccedoOneException> {
    @Override
    public ApplicationStatus parse(Response response) throws AccedoOneException {
        try {
            JSONObject jsonObject = new JSONObject(response.getText());
            ApplicationStatus.Status status = ApplicationStatus.Status.valueOf(jsonObject.getString("status").toUpperCase(Locale.US));
            String message = jsonObject.optString("message");
            return new ApplicationStatus(status, message);
        } catch (Exception e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }
}