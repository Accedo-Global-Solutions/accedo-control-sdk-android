package tv.accedo.control.model

class AccedoOneException(
    statusCode: StatusCode,
    detailMessage: String? = null,
    cause: Throwable? = null
) : Exception("$statusCode $detailMessage", cause) {
    enum class StatusCode(val message: String) {
        NO_SESSION("Could not retreive valid session."),
        NO_RESPONSE("No response from server."),
        INVALID_RESPONSE("Invalid response from server."),
        INVALID_PARAMETERS("Invalid input parameters specified for the call."),
        CACHE_MISS("Couldn't find offline cached content."),
        CACHE_ERROR("Cache error. Overriden or corrupted cache entry found."),
        KEY_NOT_FOUND("The specified key does not exist in the Accedo One configuration.");

        override fun toString(): String {
            return "$name: $message"
        }
    }
}