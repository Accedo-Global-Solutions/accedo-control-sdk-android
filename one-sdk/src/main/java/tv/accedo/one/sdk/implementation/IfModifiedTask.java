package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tv.accedo.one.sdk.BuildConfig;
import tv.accedo.one.sdk.implementation.utils.InternalStorage;
import tv.accedo.one.sdk.implementation.utils.Request;
import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.implementation.utils.Response.ThrowingParser;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.Session;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
class IfModifiedTask {
    private static final String FILENAME_VERSIONCODE = "OneSdkVersion";
    private static final String FILENAME_LAST_PROFILE_ID = "OneLastProfileId";
    private static final int LAST_CACHEBREAKING_VERSION_UPDATE = 102; //1.0(.0)

    private static final SimpleDateFormat sdfIfModifiedSince = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    private AccedoOneImpl accedoOneImpl;
    private Context context;
    private String url;

    public IfModifiedTask(AccedoOneImpl accedoOneImpl, Context context, String url) {
        this.accedoOneImpl = accedoOneImpl;
        this.context = context;
        this.url = url;
    }

    @SuppressWarnings("unchecked")
    public <O> O run(ThrowingParser<byte[], O, AccedoOneException> parser) throws AccedoOneException {
        cleanup(context, false);

        Request request = accedoOneImpl.createRestClient(url);
        String cacheKey = null;
        String timestampCacheKey = null;
        String profileId = null;

        //Try to fetch a response.. Remember, even getSession() might fail if we are offline
        Throwable caughtException = null;
        Response response = null;
        try {
            // Get session, profile, and the cachekeys for the given profile
            String sessionKey = accedoOneImpl.getSession();
            profileId = accedoOneImpl.getCurrentSession().getProfile().getProfileId();
            cacheKey = getCacheKey(request.getUrl(), accedoOneImpl.getAppKey(), profileId);
            timestampCacheKey = getTimestampCacheKey(request.getUrl(), accedoOneImpl.getAppKey(), profileId);

            //If-Modified-Since
            if (InternalStorage.exists(context, timestampCacheKey)) {
                Long lastFetchedAt = (Long) InternalStorage.read(context, timestampCacheKey);
                if (lastFetchedAt != null) {
                    request.addHeader("If-Modified-Since", sdfIfModifiedSince.format(new Date(lastFetchedAt)));
                }
            }

            //Connect
            response = request.addHeader(Constants.HEADER_SESSION, sessionKey).connect(new AccedoOneResponseChecker());

        } catch (Exception e) {
            Utils.log(e);
            Utils.log(Log.INFO, "Something went wrong. Going into offline mode for: " + request.getUrl());
            caughtException = e.getCause();
        }

        //Process response
        if (response != null && response.isSuccess()) {
            //Store if we've successfuly fetched something
            O result = parser.parse(response.getRawResponse());
            InternalStorage.write(context, response.getRawResponse(), cacheKey);
            InternalStorage.write(context, response.getServerTime(), timestampCacheKey);
            InternalStorage.write(context, profileId, FILENAME_LAST_PROFILE_ID);
            Utils.log(Log.INFO, "Storing in offline cache: " + request.getUrl());
            return result;

        } else {
            //Try from offline cache

            // See if we at least got the profileId, or we need to recover the last saved profile Id
            if (cacheKey == null || timestampCacheKey == null) {
                String lastProfileId = (String) InternalStorage.read(context, FILENAME_LAST_PROFILE_ID);
                if (lastProfileId != null) {
                    cacheKey = getCacheKey(request.getUrl(), accedoOneImpl.getAppKey(), lastProfileId);
                    timestampCacheKey = getTimestampCacheKey(request.getUrl(), accedoOneImpl.getAppKey(), lastProfileId);
                }
            }

            Object result = null;
            if (cacheKey != null && timestampCacheKey != null && InternalStorage.exists(context, cacheKey)) {
                result = parser.parse((byte[]) InternalStorage.read(context, cacheKey));
            }
            if (result != null) {
                //We got something, lets try to cast and return it
                try {
                    O o = (O) result;
                    Utils.log(Log.INFO, "Serving from offline cache: " + request.getUrl());
                    return o;
                } catch (ClassCastException e) {
                    Utils.log(Log.WARN, "Failed to serve from offline cache: " + request.getUrl());
                    InternalStorage.delete(context, cacheKey);
                    InternalStorage.delete(context, timestampCacheKey);
                    InternalStorage.delete(context, FILENAME_LAST_PROFILE_ID);
                    throw new AccedoOneException(StatusCode.CACHE_ERROR, e);
                }
            } else {
                //Sorry, no luck
                Utils.log(Log.WARN, "Failed to serve from offline cache: " + request.getUrl());
                throw new AccedoOneException(StatusCode.CACHE_MISS, caughtException);
            }
        }
    }

    private String getCacheKey(String url, String appKey, String profileId) {
        return "ONE" + Utils.md5Hash(removeSession(url) + appKey + profileId);
    }

    private String getTimestampCacheKey(String url, String appKey, String profileId) {
        return getCacheKey(url, appKey, profileId) + ".t";
    }

    public static String getCacheKey(Context context, String url, String appKey, Session session) {
        String profileId;
        if (session != null && session.getProfile() != null) {
            profileId = session.getProfile().getProfileId();
        } else {
            profileId = (String) InternalStorage.read(context, IfModifiedTask.FILENAME_LAST_PROFILE_ID);

        }
        return "ONE" + Utils.md5Hash(removeSession(url) + appKey + profileId);
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

    static void cleanup(Context context, boolean force) {
        Integer versionCode = (Integer) InternalStorage.read(context, FILENAME_VERSIONCODE);

        if (force || versionCode == null || versionCode.intValue() < LAST_CACHEBREAKING_VERSION_UPDATE) {
            for (File file : context.getFilesDir().listFiles()) {
                if (file.getName().startsWith("ONE")) {
                    file.delete();
                }
            }
        }

        InternalStorage.write(context, new Integer(BuildConfig.VERSION_CODE), FILENAME_VERSIONCODE);
    }
}