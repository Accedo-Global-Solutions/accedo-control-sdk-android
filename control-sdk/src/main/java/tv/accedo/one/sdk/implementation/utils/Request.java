/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation.utils;

import android.util.Log;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    protected HttpURLConnection httpUrlConnection;
    protected Exception caughtCreationException;
    protected byte[] payload;
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
    public Request setMethod(Method method) {
        try {
            httpUrlConnection.setRequestMethod(method.name());
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
        if (httpUrlConnection != null) {
            httpUrlConnection.setRequestProperty(key, value);
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
        if (httpUrlConnection != null) {
            httpUrlConnection.addRequestProperty(key, value);
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
        if (httpUrlConnection != null) {
            httpUrlConnection.setRequestProperty("Cookie", cookieString.toString());
        }

        return this;
    }

    /**
     * Sets the payload to be sent in the request body. The string will be processed with the charset active at the time this value is set.
     * !!To make sure that this value is encoded properly, make sure the charset is set before calling this method.!!
     *
     * @param output The string to be sent in the request body.
     * @return this RestClient instance for chaining
     */
    public Request setPayload(String payload) {
        if (payload != null) {
            try {
                this.payload = payload.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                Utils.log(e);
            }
        } else {
            this.payload = null;
        }

        return this;
    }

    /**
     * Sets the payload to be sent in the request body.
     *
     * @param rawOutput The data to be sent in the request body.
     * @return this RestClient instance for chaining
     */
    public Request setPayload(byte[] payload) {
        this.payload = payload;
        return null;
    }

    /**
     * Sets the timeouts for the request.
     *
     * @param connect the timeout value for the connection to be established
     * @param read    the timeout value for the fetching operation to be completed
     * @return this RestClient instance for chaining
     */
    public Request setTimeout(int connect, int read) {
        if (httpUrlConnection != null) {
            httpUrlConnection.setConnectTimeout(connect);
            httpUrlConnection.setReadTimeout(read);
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
            httpUrlConnection = (HttpURLConnection) (new URL(url).openConnection());
        } catch (Exception e) {
            caughtCreationException = e;
            Utils.log(e);
        }

        //Init httpUrlConnection
        if (httpUrlConnection != null) {
            httpUrlConnection.setConnectTimeout(DEFAULT_TIMEOUT_CONNECT);
            httpUrlConnection.setReadTimeout(DEFAULT_TIMEOUT_READ);
            httpUrlConnection.setUseCaches(false);
        }
    }

    public <E extends Exception> Response connect(ResponseChecker<E> responseChecker) throws E {
        Response response = fetchResponse();

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

    private Response fetchResponse() {
        Response response = new Response(url, caughtCreationException);
        OutputStream os = null;

        if (caughtCreationException == null && httpUrlConnection != null && httpUrlConnection.getURL() != null) {
            try {
                //Logging
                Utils.log(Log.DEBUG, "Sending " + httpUrlConnection.getRequestMethod() + " request: " + httpUrlConnection.getURL().toString());

                //Request
                httpUrlConnection.connect();
                if (payload != null) {
                    os = httpUrlConnection.getOutputStream();
                    os.write(payload);
                    os.flush();
                    os.close();
                }
                response = new Response(httpUrlConnection, url, charset);

            } catch (Exception e) {
                Utils.log(e);
                response = new Response(url, e);
            } finally {
                Utils.closeQuietly(os);
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