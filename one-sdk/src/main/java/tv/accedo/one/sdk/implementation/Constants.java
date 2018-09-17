package tv.accedo.one.sdk.implementation;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
class Constants {
    static final String HEADER_SESSION = "X-Session";
    static final String HEADER_APPKEY = "X-Application-Key";
    static final String HEADER_USERID = "X-User-Id";

    static final String PATH_SESSION = "/session";
    static final String PATH_PROFILE = "/profile";
    static final String PATH_STATUS = "/status";
    static final String PATH_LOG_APPEVENT = "/event/log";
    static final String PATH_LOGS = "/application/logs/";
    static final String PATH_LOG_LEVEL = "/application/log/level";
    static final String PATH_METADATA = "/metadata/";
    static final String PATH_ASSETS = "/asset";

    static final String PATH_ENTRY = "/content/entry/";
    static final String PATH_ENTRY_BY_ALIAS = "/content/entry/alias/";
    static final String PATH_ENTRIES = "/content/entries";
    static final String PATH_LOCALES = "/locales";

    static final String EVENT_START = "START";
    static final String EVENT_QUIT = "QUIT";

    public static final long   DEFAULT_LOGLEVEL_INVALIDATION_PERIOD = 15 * 60 * 1000;
    public static final long   DEFAULT_LOGGING_PERIOD = 3 * 60 * 1000;
    public static final String DEFAULT_ENDPOINT = "https://api.one.accedo.tv";
}