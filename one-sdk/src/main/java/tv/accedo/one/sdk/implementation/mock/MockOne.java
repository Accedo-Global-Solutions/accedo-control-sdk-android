package tv.accedo.one.sdk.implementation.mock;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
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
import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.model.ApplicationStatus;
import tv.accedo.one.sdk.model.ApplicationStatus.Status;
import tv.accedo.one.sdk.model.LogEntry;
import tv.accedo.one.sdk.model.LogLevel;
import tv.accedo.one.sdk.model.Profile;
import tv.accedo.one.sdk.implementation.parsers.JSONMapByteParser;
import tv.accedo.one.sdk.definition.async.Cancellable;
import tv.accedo.one.sdk.implementation.utils.Utils;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class MockOne implements AccedoOne, AccedoOneControl, AccedoOneDetect, AccedoOneInsight {
    private static final String URL_ASSET_BASE = "file:///android_asset/";
    private final ConfigSource configSource;

    private JSONObject metadata;
    private Map<String, String> assets;

    /**
     * @param configSource custom source for configuration that may be either local or remote
     */
    public MockOne(ConfigSource configSource) {
        this.configSource = configSource;
    }

    /**
     * @param metadataFile the filename of the file in the assets folder representing metadata. The file should contain a JSONObject containing key-value pairs that represent all Accedo One metadata.
     * @param assetsFile the filename of the file in the assets folder representing assets. The file should contain a JSONObject containing key-value pairs that represent Accedo One asset URL-s. Files in the Android assets folder should be represented as file:///android_asset/myfile
     * @param entriesFile the filename of the file in the assets folder representing Publish entries. The file should containa JSONArray containing a list of JSONObjects that represent all Accedo One Publish entries
     */
    public MockOne(final String metadataFile, final String assetsFile, final String entriesFile) {
        this.configSource = new ConfigSource() {
            @Override
            public JSONObject getAllMetadata(Context context) throws Exception {
                if (metadataFile == null) {
                    return new JSONObject();
                }
                return new JSONObject(Utils.toString(context.getAssets().open(metadataFile)));
            }

            @Override
            public JSONArray getAllEntries(Context context) throws Exception {
                if (entriesFile == null) {
                    return new JSONArray();
                }
                return new JSONArray(Utils.toString(context.getAssets().open(entriesFile)));
            }

            @Override
            public Map<String, String> getAllAssets(Context context) throws Exception {
                if (assetsFile == null) {
                    return new HashMap<>();
                }
                return new JSONMapByteParser().parse(Utils.toByteArray(context.getAssets().open(assetsFile)));
            }
        };
    }

    @Override
    public String getEndpoint() {
        return "mock";
    }

    @Override
    public String getDeviceId() {
        return "mock";
    }

    @Override
    public String getGid() {
        return null;
    }

    @Override
    public String getAppKey() {
        return "mock";
    }

    @Override
    public long getServerTime() {
        return System.currentTimeMillis();
    }

    @Override
    public Cancellable applicationStart() {
        return null;
    }

    @Override
    public Cancellable applicationQuit() {
        return null;
    }

    @Override
    public Cancellable applicationQuit(int retentionSeconds) {
        return null;
    }

    @Override
    public void log(LogLevel logLevel, int code, String message, String... dimensions) {
        LogEntry logEntry = new LogEntry(logLevel, getServerTime(), code, message, dimensions);

        if (logLevel == null || code < 0 || code > 99999 || message == null || (dimensions != null && dimensions.length > 4)) {
            Utils.log(Log.WARN, "Invalid logEntry, skipping: " + logEntry.toString());
        } else {
            logEntry.toLogCat();
        }
    }

    @Override
    public Cancellable purgeLogs() {
        return null;
    }

    @Override
    public Profile getProfile(Context context) throws AccedoOneException {
        return new Profile();
    }

    @Override
    public ApplicationStatus getApplicationStatus(Context context) {
        return new ApplicationStatus(Status.ACTIVE, "");
    }

    @Override
    public String getMetadata(Context context, String key) throws AccedoOneException {
        return getAllMetadata(context).get(key);
    }

    @Override
    public Map<String, String> getAllMetadata(Context context) throws AccedoOneException {
        return new JSONMapByteParser().parse(getAllMetadataRaw(context).toString().getBytes());
    }

    @Override
    public JSONObject getAllMetadataRaw(Context context) throws AccedoOneException {
        if (metadata == null) {
            try {
                metadata = configSource.getAllMetadata(context);
            } catch (Exception e) {
                throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
            }
        }
        return metadata;
    }

    @Override
    public byte[] getAsset(Context context, String key) throws AccedoOneException {
        String url = getAllAssets(context).get(key);
        if (url == null) {
            throw new AccedoOneException(StatusCode.KEY_NOT_FOUND);
        }

        //Get as Android asset
        if (url.startsWith(URL_ASSET_BASE)) {
            try {
                String filename = url.substring(URL_ASSET_BASE.length());
                return Utils.toByteArray(context.getAssets().open(filename));
            } catch (IOException e) {
                throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
            }
        }

        //Get as url
        try {
            return Utils.toByteArray(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
        }
    }

    @Override
    public Map<String, String> getAllAssets(Context context) throws AccedoOneException {
        if (assets == null) {
            try {
                assets = configSource.getAllAssets(context);
            } catch (Exception e) {
                throw new AccedoOneException(StatusCode.INVALID_RESPONSE, e);
            }
        }
        return assets;
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
    public AsyncAccedoOneControl async() {
        return new AsyncAccedoOneControlImpl(this);
    }

    @Override
    public AccedoOneControl control() {
        return this;
    }

    @Override
    public AccedoOneDetect detect() {
        return this;
    }

    @Override
    public AccedoOneInsight insight() {
        return this;
    }

    @Override
    public AccedoOneUserData userData() {
        return new MockOneUserData();
    }

    @Override
    public AccedoOnePublish publish() {
        return new MockOnePublish(configSource);
    }

    @Override
    public AccedoOneCache cache() {
        throw new UnsupportedOperationException("MockGridService does not support direct cache access.");
    }
}