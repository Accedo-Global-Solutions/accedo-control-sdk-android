package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import tv.accedo.one.sdk.implementation.utils.InternalStorage;
import tv.accedo.one.sdk.implementation.utils.Request;
import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
class IfModifiedTask {
    private static final String FILENAME_VERSIONCODE = "OneSdkVersion";
    private static final int LAST_CACHEBREAKING_VERSION_UPDATE = 100; //1.0(.0)

    static final SimpleDateFormat sdfIfModifiedSince = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    static {
        sdfIfModifiedSince.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @NonNull
    private final AccedoOneImpl accedoOneImpl;
    @NonNull
    private final Context context;
    @NonNull
    private final String url;

    public IfModifiedTask(@NonNull AccedoOneImpl accedoOneImpl, @NonNull Context context, @NonNull String url) {
        this.accedoOneImpl = accedoOneImpl;
        this.context = context;
        this.url = url;
    }

    public <O> O run(ThrowingParser<byte[], O, AccedoOneException> parser) throws AccedoOneException {
        cleanup(context, false);

        Request request = accedoOneImpl.createRestClient(url);

        String key = getCacheKey(request.getUrl(), accedoOneImpl.getAppKey(), accedoOneImpl.getGid());
        String timestampKey = getTimestampCacheKey(request.getUrl(), accedoOneImpl.getAppKey(), accedoOneImpl.getGid());

        //Try to fetch a response.. Remember, even createSessionedRestClient() might fail if we are offline
        Throwable caughtException = null;
        Response response = null;
        try {
            //If-Modified-Since
            if (InternalStorage.exists(context, timestampKey)) {
                Long lastFetchedAt = (Long) InternalStorage.read(context, timestampKey);
                if (lastFetchedAt != null) {
                    request.addHeader("If-Modified-Since", sdfIfModifiedSince.format(new Date(lastFetchedAt)));
                }
            }

            //Connect
            response = request.addHeader(Constants.HEADER_SESSION, accedoOneImpl.getSession()).connect(accedoOneImpl.okHttpClient, new AccedoOneResponseChecker());

        } catch (AccedoOneException e) {
            Utils.log(e);
            Utils.log(Log.INFO, "Something went wrong. Going into offline mode for: " + request.getUrl());
            caughtException = e.getCause();
        }

        //Process response
        if (response != null && response.isSuccess() && response.getRawResponse() != null) {
            //Store if we've successfuly fetched something
            O result = parser.parse(response.getRawResponse());
            InternalStorage.write(context, response.getRawResponse(), key);
            InternalStorage.write(context, response.getServerTime(), timestampKey);
            Utils.log(Log.INFO, "Storing in offline cache: " + request.getUrl());
            return result;

        } else {
            //Try from offline cache
            O result = null;
            if (InternalStorage.exists(context, key)) {
                result = parser.parse((byte[]) InternalStorage.read(context, key));
            }
            if (result != null) {
                //We got something, lets try to cast and return it
                try {
                    Utils.log(Log.INFO, "Serving from offline cache: " + request.getUrl());
                    return result;
                } catch (ClassCastException e) {
                    Utils.log(Log.WARN, "Failed to serve from offline cache: " + request.getUrl());
                    InternalStorage.delete(context, key);
                    InternalStorage.delete(context, timestampKey);
                    throw new AccedoOneException(StatusCode.CACHE_ERROR, e);
                }
            } else {
                //Sorry, no luck
                Utils.log(Log.WARN, "Failed to serve from offline cache: " + request.getUrl());
                throw new AccedoOneException(StatusCode.CACHE_MISS, caughtException);
            }
        }
    }

    public static String getCacheKey(String url, String appKey, String gid) {
        return "ONE" + Utils.md5Hash(removeSession(url) + appKey + gid);
    }

    public static String getTimestampCacheKey(String url, String appKey, String gid) {
        return "ONE" + Utils.md5Hash(removeSession(url) + appKey + gid) + ".t";
    }

    private static String removeSession(String url) {
        if (url.contains("sessionKey")) {
            try {
                URI uri = new URI(url);
                return new URI(uri.getScheme(),
                        uri.getAuthority(),
                        uri.getPath(),
                        null, // ignore the query part of the input url, contains sessionKey
                        uri.getFragment()).toString();
            } catch (URISyntaxException e) {
                Utils.log(e);
            }
        }

        return url;
    }

    static void cleanup(@NonNull Context context, boolean force) {
        try {
            Integer versionCode = (Integer) InternalStorage.read(context, FILENAME_VERSIONCODE);

            if (force || versionCode == null || versionCode < LAST_CACHEBREAKING_VERSION_UPDATE) {
                for (File file : Objects.requireNonNull(context.getFilesDir().listFiles())) {
                    if (file.getName().startsWith("ONE")) {
                        file.delete();
                    }
                }
            }

            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            InternalStorage.write(context, pInfo.versionCode, FILENAME_VERSIONCODE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
