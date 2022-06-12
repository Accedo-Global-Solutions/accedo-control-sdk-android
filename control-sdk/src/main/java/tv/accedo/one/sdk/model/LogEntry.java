package tv.accedo.one.sdk.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import tv.accedo.one.sdk.implementation.utils.Utils;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class LogEntry {
    @NonNull
    private final LogLevel logLevel;
    private final long timestamp;
    private final int code;
    @Nullable
    private final String message;
    @Nullable
    private final String[] dimensions;

    @NonNull
    public LogLevel getLogLevel() {
        return logLevel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String[] getDimensions() {
        return dimensions;
    }

    public LogEntry(@NonNull LogLevel logLevel, long timestamp, int code, @Nullable String message, @Nullable String... dimensions) {
        this.logLevel = logLevel;
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.dimensions = dimensions;
    }

    public void toLogCat() {
        Utils.log(logLevel.logcatPriority, toString());
    }

    public void toLogCat(String message) {
        Utils.log(logLevel.logcatPriority, message + " " + toString());
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("logType", logLevel.name());
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("code", code);
            jsonObject.put("message", message);
            for (int i = 0; i < dimensions.length && i < 4; i++) {
                jsonObject.put("dim" + (i + 1), dimensions[i]);
            }
            return jsonObject;

        } catch (JSONException e) {
            Utils.log(e); //Only happens if you call put with a null key which i don't do now.
            return new JSONObject();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntry)) return false;

        LogEntry logEntry = (LogEntry) o;

        if (timestamp != logEntry.timestamp) return false;
        if (code != logEntry.code) return false;
        if (logLevel != logEntry.logLevel) return false;
        if (message != null ? !message.equals(logEntry.message) : logEntry.message != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(dimensions, logEntry.dimensions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = logLevel.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + code;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(dimensions);
        return result;
    }
}
