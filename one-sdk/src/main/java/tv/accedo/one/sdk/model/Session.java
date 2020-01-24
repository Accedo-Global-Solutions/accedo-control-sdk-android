package tv.accedo.one.sdk.model;

import java.io.Serializable;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class Session implements Serializable {
    private String sessionKey;
    private long sessionExpiration;
    private Profile profile;

    public Session(String sessionKey, long sessionExpiration, Profile profile) {
        this.sessionKey = sessionKey;
        this.sessionExpiration = sessionExpiration;
        this.profile = profile;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public long getSessionExpiration() {
        return sessionExpiration;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return sessionKey.equals(session.sessionKey);
    }

    @Override
    public int hashCode() {
        return sessionKey.hashCode();
    }
}