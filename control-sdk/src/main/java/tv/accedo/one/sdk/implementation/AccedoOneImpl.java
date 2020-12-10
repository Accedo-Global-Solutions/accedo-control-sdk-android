package tv.accedo.one.sdk.implementation;

import android.content.Context;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import tv.accedo.one.sdk.definition.AccedoOneCache;
import tv.accedo.one.sdk.definition.AccedoOneControl;
import tv.accedo.one.sdk.definition.AccedoOneInsight;
import tv.accedo.one.sdk.definition.AccedoOnePublish;
import tv.accedo.one.sdk.definition.AccedoOneDetect;
import tv.accedo.one.sdk.definition.AccedoOne;
import tv.accedo.one.sdk.definition.AccedoOneUserData;
import tv.accedo.one.sdk.definition.async.AsyncAccedoOneControl;
import tv.accedo.one.sdk.implementation.async.AsyncAccedoOneControlImpl;
import tv.accedo.one.sdk.implementation.utils.Utils;
import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.implementation.utils.Request;
import tv.accedo.one.sdk.implementation.utils.Request.OnResponseListener;
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.ApplicationStatus;
import tv.accedo.one.sdk.model.ApplicationStatus.Status;
import tv.accedo.one.sdk.model.Profile;
import tv.accedo.one.sdk.implementation.parsers.ApplicationStatusParser;
import tv.accedo.one.sdk.implementation.parsers.ByteArrayParser;
import tv.accedo.one.sdk.implementation.parsers.JSONMapByteParser;
import tv.accedo.one.sdk.implementation.parsers.JSONObjectByteParser;
import tv.accedo.one.sdk.implementation.parsers.ProfileParser;
import tv.accedo.one.sdk.implementation.parsers.SessionParser;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneImpl extends Constants implements AccedoOne, AccedoOneControl {
    //Server time
    private long serverTimeDifference = System.currentTimeMillis() - SystemClock.elapsedRealtime();

    //Incoming parameters
    private String endpoint = DEFAULT_ENDPOINT;
    private String appKey;
    private String deviceId;
    private String gid;

    //Storage
    private ConditionVariable cvSession = new ConditionVariable(true);
    private Pair<String, Long> session;

    //Subservices
    private AccedoOneDetectImpl accedoOneDetectImpl = new AccedoOneDetectImpl(this);

    /**
     * Should only be called right after initialising the service.
     *
     * @param endpoint the url of the Accedo One server used. The default value is stored in DEFAULT_ENDPOINT.
     * @return
     */
    public AccedoOneImpl setEndpoint(String endpoint) {
        throwIfSessionInitialised();
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Should only be called right after initialising the service.
     *
     * @param deviceId the deviceId sent to Accedo One. The default value is DeviceIdentifier.getDeviceId(context);
     * @return
     */
    public AccedoOneImpl setDeviceId(String deviceId) {
        throwIfSessionInitialised();
        this.deviceId = deviceId;
        return this;
    }

    /**
     * Should only be called right after initialising the service. Changing the GID within one session is unsupported.
     *
     * @param gid an optional global identifier used for whitelisting.
     * @return
     */
    public AccedoOneImpl setGid(String gid) {
        throwIfSessionInitialised();
        this.gid = gid;
        return this;
    }

    /**
     * Should only be called right after initialising the service.
     *
     * @param loggingPeriod the amount of time the app waits before sending out logs, so it can batch log requests, in milliseconds.
     */
    public AccedoOneImpl setLoggingPeriod(long loggingPeriod) {
        accedoOneDetectImpl.setLoggingPeriod(loggingPeriod);
        return this;
    }

    /**
     * Should only be called right after initialising the service.
     *
     * @param loglevelInvalidation the amount of time this service should wait between fetching the currently set log level from Accedo One, in milliseconds.
     * @return
     */
    public AccedoOneImpl setLoglevelInvalidation(long loglevelInvalidation) {
        accedoOneDetectImpl.setLoglevelInvalidation(loglevelInvalidation);
        return this;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getGid() {
        return gid;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    @Override
    public long getServerTime() {
        return SystemClock.elapsedRealtime() + serverTimeDifference;
    }

    /**
     * This constructor uses the default endpoint of {@link Constants.DEFAULT_ENDPOINT}
     *
     * @param appKey the hash of your Application inside Accedo One to connect to.
     * @param deviceId a unique identifier of your device. (Eg AndroidID)
     */
    public AccedoOneImpl(String appKey, String deviceId) {
        this.appKey = appKey;
        this.deviceId = deviceId;
    }

    /**
     * @param endpoint The endpoint to connect to. The default is {@link Constants.DEFAULT_ENDPOINT}.
     * @param appKey the hash of your Application inside Accedo One to connect to.
     * @param deviceId a unique identifier of your device. (Eg AndroidID)
     */
    public AccedoOneImpl(String endpoint, String appKey, String deviceId) {
        this.endpoint = endpoint;
        this.appKey = appKey;
        this.deviceId = deviceId;
    }

    //-------- metadata --------
    @Override
    public String getMetadata(Context context, String key) throws AccedoOneException {
        Map<String, String> allMetadata = getAllMetadata(context);
        if (allMetadata.containsKey(key)) {
            return allMetadata.get(key);
        } else {
            throw new AccedoOneException(StatusCode.KEY_NOT_FOUND);
        }
    }

    @Override
    public Map<String, String> getAllMetadata(Context context) throws AccedoOneException {
        String url = endpoint + PATH_METADATA;
        return new IfModifiedTask(this, context, url).run(new JSONMapByteParser());
    }

    @Override
    public JSONObject getAllMetadataRaw(Context context) throws AccedoOneException {
        String url = endpoint + PATH_METADATA;
        return new IfModifiedTask(this, context, url).run(new JSONObjectByteParser());
    }

    //-------- asset --------
    @Override
    public byte[] getAsset(Context context, String key) throws AccedoOneException {
        String url = getAllAssets(context).get(key);
        if (url == null) {
            throw new AccedoOneException(StatusCode.KEY_NOT_FOUND);
        }

        return new IfModifiedTask(this, context, url).run(new ByteArrayParser());
    }

    @Override
    public Map<String, String> getAllAssets(Context context) throws AccedoOneException {
        String url = endpoint + PATH_ASSETS;
        return new IfModifiedTask(this, context, url).run(new JSONMapByteParser());
    }

    @Override
    public Map<String, byte[]> getAllAssetsRaw(Context context) throws AccedoOneException {
        HashMap<String, byte[]> result = new HashMap<>();

        Map<String, String> assets = getAllAssets(context);
        for (Entry<String, String> entry : assets.entrySet()) {
            result.put(entry.getKey(), getAsset(context, entry.getKey()));
        }

        return result;
    }

    @Override
    public Profile getProfile(Context context) throws AccedoOneException {
        return createSessionedRestClient(endpoint + PATH_PROFILE)
                .connect(new AccedoOneResponseChecker())
                .getParsedText(new ProfileParser());
    }

    @Override
    public ApplicationStatus getApplicationStatus(Context context) {
        try {
            return createSessionedRestClient(endpoint + PATH_STATUS)
                    .connect(new AccedoOneResponseChecker())
                    .getParsedText(new ApplicationStatusParser());
        } catch (AccedoOneException e) {
            Utils.log(e);
            return new ApplicationStatus(Status.UNKNOWN, null);
        }
    }

    public String getSession() throws AccedoOneException {
        //If we have session and its valid, return
        cvSession.block();
        if (session != null && session.second > getServerTime()) {
            return session.first;
        }

        //Otherwise fetch and store
        cvSession.close();
        try {
            Response response = createRestClient(endpoint + PATH_SESSION)
                    .addHeader(HEADER_APPKEY, appKey)
                    .addHeader(HEADER_USERID, deviceId)
                    .connect(new AccedoOneResponseChecker());

            serverTimeDifference = response.getServerTime() - SystemClock.elapsedRealtime();
            session = response.getParsedText(new SessionParser());
        } catch (AccedoOneException e) {
            throw e;
        } finally {
            cvSession.open();
        }

        //And return
        return session.first;
    }

    @Override
    public AsyncAccedoOneControl async() {
        return new AsyncAccedoOneControlImpl(this);
    }

    @Override
    public AccedoOneControl control() {
        return this;
    }

    @Override
    public AccedoOneDetect detect() {
        return accedoOneDetectImpl;
    }

    @Override
    public AccedoOneInsight insight() {
        return new AccedoOneInsightImpl(this);
    }

    @Override
    public AccedoOneUserData userData() {
        return new AccedoOneUserDataImpl(this);
    }

    @Override
    public AccedoOnePublish publish() {
        return new AccedoOnePublishImpl(this);
    }

    @Override
    public AccedoOneCache cache() {
        return new AccedoOneCacheImpl(this);
    }

    public Request createRestClient(String url) {
        Uri uri = Uri.parse(url);
        if (!TextUtils.isEmpty(gid)) {
            uri = uri.buildUpon().appendQueryParameter("gid", gid).build();
        }
        return new Request(uri.toString());
    }

    public Request createSessionedRestClient(String url) throws AccedoOneException {
        return createRestClient(url).addHeader(HEADER_SESSION, getSession());
    }

    private void throwIfSessionInitialised() {
        if (session != null) {
            throw new IllegalStateException("Service is already initialised. You can only call setEndpoint() before the first call is made.");
        }
    }
}