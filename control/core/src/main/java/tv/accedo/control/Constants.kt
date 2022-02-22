package tv.accedo.control

object Constants {
    const val HEADER_SESSION = "X-Session"
    const val HEADER_APPKEY = "X-Application-Key"
    const val HEADER_USERID = "X-User-Id"

    const val PATH_SESSION = "/session"
    const val PATH_PROFILE = "/profile"
    const val PATH_STATUS = "/status"
    const val PATH_LOG_APPEVENT = "/event/log"
    const val PATH_LOGS = "/application/logs/"
    const val PATH_LOG_LEVEL = "/application/log/level"
    const val PATH_METADATA = "/metadata/"
    const val PATH_ASSETS = "/asset"

    const val PATH_ENTRY = "/content/entry/"
    const val PATH_ENTRY_BY_ALIAS = "/content/entry/alias/"
    const val PATH_ENTRIES = "/content/entries"
    const val PATH_LOCALES = "/locales"

    const val EVENT_START = "START"
    const val EVENT_QUIT = "QUIT"

    const val DEFAULT_LOGLEVEL_INVALIDATION_PERIOD = (15 * 60 * 1000).toLong()
    const val DEFAULT_LOGGING_PERIOD = (3 * 60 * 1000).toLong()
    const val DEFAULT_ENDPOINT = "https://api.one.accedo.tv"
}