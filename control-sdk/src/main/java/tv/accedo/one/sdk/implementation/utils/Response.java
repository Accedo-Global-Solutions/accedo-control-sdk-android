/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class Response {
    public static final DateFormat DATE_HEADER_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);

    static {
        DATE_HEADER_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private int code = -1;
    private byte[] response;
    private Charset charset = StandardCharsets.UTF_8;
    @NonNull
    private final String url;
    private final Map<String, List<String>> headers = new HashMap<>();
    @Nullable
    private Exception caughtException;

    /**
     * @return the response code, or -1 if no connection was made
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the response body, string encoded with the charset of the RestClient instance, that created this Response
     */
    @Nullable
    public String getText() {
        try {
            return new String(response, charset);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the raw response body
     */
    @Nullable
    public byte[] getRawResponse() {
        return response;
    }

    /**
     * @return true, if the response code is 200 or 204
     */
    public boolean isSuccess() {
        return code == 200 || code == 204;
    }

    /**
     * @return the url fetched
     */
    @NonNull
    public String getUrl() {
        return url;
    }

    /**
     * @return a map of response headers, or an empty map if no connection was established (never null)
     */
    @NonNull
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * @param name the name of the header to find
     * @return the first value for that header or an empty string
     */
    public String getFirstHeader(String name) {
        List<String> values = headers.get(name.toLowerCase(Locale.US));
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        }

        return "";
    }

    /**
     * @return the parsed UTC server time from the "Date" header. If there's no Date header, or its malformed, System.currentTimeMillis() is returned.
     */
    public long getServerTime() {
        try {
            String dateHeader = getFirstHeader("Date");
            return DATE_HEADER_FORMAT.parse(dateHeader).getTime();
        } catch (ParseException e) {
            Utils.log(Log.WARN, "Failed to parse server time from Date header, returning device time.");
            Utils.log(e);
            return System.currentTimeMillis();
        }
    }

    /**
     * @return The exception caught during the creation of the urlConnection used, or during connection, or during the parsing of the response.
     */
    @Nullable
    public Exception getCaughtException() {
        return caughtException;
    }

    /**
     * The constructor to use when the connection was successful.
     *
     * @param okHttpResponse actual response from OkHttp3.
     * @param url            the url we were connecting to.
     * @param charset        the charset used, the default being {@link Request.charset}.
     */
    public Response(@NonNull okhttp3.Response okHttpResponse, @NonNull String url, @NonNull Charset charset) {
        this.url = url;
        this.charset = charset;

        //Code & Body
        try {
            code = okHttpResponse.code();
            response = okHttpResponse.body().bytes();

        } catch (Exception e) {
            this.caughtException = e;
        }

        //Headers
        headers.putAll(okHttpResponse.headers().toMultimap());

        //Logging
        Utils.log(Log.DEBUG, "Response " + code + " for: " + getUrl() + "\n" + getText() + "\n" + "headers: " + getHeaders());
    }

    /**
     * The constructor to use when connection has failed.
     *
     * @param url    the url we wanted to connect to.
     * @param reason the reason why we couldn't connect.
     */
    public Response(@NonNull String url, @Nullable Exception reason) {
        this.url = url;
        this.caughtException = reason;
    }

    /**
     * Domain-parses this response with the supplied parser, or throws an exception on failure
     *
     * @param parser
     * @return The domain parsed variant of this Response's body
     * @throws Exception, thrown by the parser.
     */
    public <T, E extends Exception> T getParsedText(ThrowingParser<Response, T, E> parser) throws E {
        return parser.parse(this);
    }

    public interface ThrowingParser<I, O, E extends Exception> {
        /**
         * Parses <I>, into <O>, or throws an <E> exception if any error occures
         *
         * @param <I> The input type
         * @param <O> The output type
         * @throws <E> The error type
         */
        O parse(@Nullable I input) throws E;
    }

}
