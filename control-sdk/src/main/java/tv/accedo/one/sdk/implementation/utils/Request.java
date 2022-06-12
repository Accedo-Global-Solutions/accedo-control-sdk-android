/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation.utils;

import android.util.Log;


import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class Request {
    public static final String TAG = "RestClient";
    protected static final int DEFAULT_TIMEOUT_CONNECT = 5000;
    protected static final int DEFAULT_TIMEOUT_READ = 10000;

    public static enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    protected String url;

    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();

    protected Exception caughtCreationException;
    protected String charset = "UTF-8";
    protected ArrayList<HttpCookie> cookies = new ArrayList<HttpCookie>();

    protected OnResponseListener onResponseListener;

    /**
     * @return the url this RestClient instance is trying to fetch
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the HTTP method of the call.
     *
     * @param method GET, POST, PUT or DELETE
     * @return this RestClient instance for chaining
     */
    public Request setMethod(Method method, @Nullable String payload) {
        try {
            RequestBody body = null;
            if (payload != null && payload.isEmpty()) {
                body = RequestBody.create(payload.getBytes(charset));
            }
            requestBuilder.method(method.name(), body);
        } catch (Exception e) {
            Utils.log(e);
        }
        return this;
    }

    /**
     * Sets the value of the specified request header field.
     *
     * @param key   the header name to be added
     * @param value the header value to be added
     * @return this RestClient instance for chaining
     */
    public Request setHeader(String key, String value) {
        if (requestBuilder != null) {
            requestBuilder.header(key, value);
        }
        return this;
    }

    /**
     * Adds the given property to the request header. Existing properties with the same name will not be overwritten by this method.
     *
     * @param key   the header name to be added
     * @param value the header value to be added
     * @return this RestClient instance for chaining
     */
    public Request addHeader(String key, String value) {
        if (requestBuilder != null) {
            requestBuilder.addHeader(key, value);
        }
        return this;
    }

    /**
     * Sets the given cookies to the request as a "Cookie" header. All previously set cookies will be removed.
     *
     * @param cookies the cookies to be added
     * @return this RestClient instance for chaining
     */
    public Request setCookies(List<HttpCookie> cookies) {
        this.cookies.clear();
        return addCookies(cookies);
    }

    /**
     * Appends the given cookies to the request's "Cookie" header.
     *
     * @param cookies the cookies to be added
     * @return this RestClient instance for chaining
     */
    public Request addCookies(List<HttpCookie> cookies) {
        if (cookies == null)
            return this;

        //Append list
        this.cookies.addAll(cookies);

        //Build current cookie string
        StringBuilder cookieString = new StringBuilder();
        for (HttpCookie cookie : this.cookies) {
            if (cookie != null) {
                cookieString.append(String.format("%s=\"%s\"; ", cookie.getName(), cookie.getValue()));
            }
        }

        //Set to urlConnection
        if (requestBuilder != null) {
            requestBuilder.addHeader("Cookie", cookieString.toString());
        }

        return this;
    }

    /**
     * Sets the character set to be used while processing the request. The default value is UTF-8.
     *
     * @param charset the RestClient instance should use. Doesn't accept null.
     * @return this RestClient instance for chaining
     */
    public Request setCharset(String charset) {
        if (charset != null) {
            this.charset = charset;
        }
        return this;
    }

    /**
     * Sets a response listener to be called when a response is obtained.
     *
     * @param onResponseListener
     * @return this RestClient instance for chaining
     */
    public Request setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
        return this;
    }

    /**
     * Creates a non-cached RestClient instance.
     *
     * @param url          the url to load
     * @param httpCacheDir the folder to use for httpCaching. Recommended: new File(context.getCacheDir(), "http")
     */
    public Request(String url) {
        this.url = url;

        //Make HttpUrlConnection
        try {
            requestBuilder = new okhttp3.Request.Builder()
                    .url(url);
        } catch (Exception e) {
            caughtCreationException = e;
            Utils.log(e);
        }


    }

    public <E extends Exception> Response connect(OkHttpClient okHttpClient,ResponseChecker<E> responseChecker) throws E {
        Response response = fetchResponse(okHttpClient);

        //Check response, and throw if necessary
        if (responseChecker != null) {
            responseChecker.throwIfNecessary(response);
        }

        //Notify listeners if we're still here
        if (onResponseListener != null) {
            onResponseListener.onResponse(response);
        }

        return response;
    }

    private Response fetchResponse(OkHttpClient okHttpClient) {
        Response response = new Response(url, caughtCreationException);

        okhttp3.Request req = requestBuilder.build();

        if (caughtCreationException == null) {
            req.url();
            try {
                //Logging
                Utils.log(Log.DEBUG, "Sending " + req.method() + " request: " + req.url());

                //Request
                okhttp3.Response okHttpResponse = okHttpClient.newCall(req).execute();

                response = new Response(okHttpResponse, url, charset);

            } catch (Exception e) {
                Utils.log(e);
                response = new Response(url, e);
            }

        }

        return response;
    }

    public static interface OnResponseListener {
        public void onResponse(Response response);
    }

    public static interface ResponseChecker<T extends Exception> {
        public void throwIfNecessary(Response response) throws T;
    }
}
