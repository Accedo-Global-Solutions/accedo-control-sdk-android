package tv.accedo.one.sdk.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class ApplicationStatus implements Serializable {
    public static enum Status{ ACTIVE, MAINTENANCE, UNKNOWN }

    @NonNull private final Status status;
    @Nullable private final String message;
	
	@NonNull
    public Status getStatus() {
		return status;
	}
	@Nullable
    public String getMessage() {
		return message;
	}
	
    public ApplicationStatus(@NonNull Status status, @Nullable String message) {
        this.status = status;
        this.message = message;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + status.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApplicationStatus other = (ApplicationStatus) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        return status == other.status;
    }

    @Override
    public String toString() {
        return "status: " + status + '\n' +
                "message: " + message;
    }
}
