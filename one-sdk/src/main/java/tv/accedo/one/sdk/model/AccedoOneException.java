package tv.accedo.one.sdk.model;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class AccedoOneException extends Exception {
    public static enum StatusCode {
        NO_SESSION("Could not retreive valid session."),
        NO_RESPONSE("No response from server."),
        INVALID_RESPONSE("Invalid response from server."),
        INVALID_PARAMETERS("Invalid input parameters specified for the call."),
        CACHE_MISS("Couldn't find offline cached content."),
        CACHE_ERROR("Cache error. Overriden or corrupted cache entry found."),
        KEY_NOT_FOUND("The specified key does not exist in the Accedo One configuration.");

        public final String message;

        StatusCode(String message){
            this.message = message;
        }

        @Override
        public String toString() {
            return name() + ": " + message;
        }
    }
    public final StatusCode statusCode;
    public final String detailMessage;

    public AccedoOneException(StatusCode statusCode) {
        super(statusCode.toString());
        this.statusCode = statusCode;
        this.detailMessage = null;
    }

    public AccedoOneException(StatusCode statusCode, Throwable reason) {
        super(statusCode.toString(), reason);
        this.statusCode = statusCode;
        this.detailMessage = null;
    }

    public AccedoOneException(StatusCode statusCode, String detailMessage) {
        super(statusCode.toString() + " " + detailMessage);
        this.statusCode = statusCode;
        this.detailMessage = detailMessage;
    }

    public AccedoOneException(StatusCode statusCode, String detailMessage, Throwable reason) {
        super(statusCode.toString() + " " + detailMessage, reason);
        this.statusCode = statusCode;
        this.detailMessage = detailMessage;
    }

    @Override
    public String toString() {
        return "statusCode: " + statusCode + '\n' +
                "detailMessage: " + detailMessage;
    }
}