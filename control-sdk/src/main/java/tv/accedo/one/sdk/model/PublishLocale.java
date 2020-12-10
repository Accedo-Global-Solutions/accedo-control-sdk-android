package tv.accedo.one.sdk.model;

import java.io.Serializable;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class PublishLocale implements Serializable{
    private String code;
    private String displayName;

    public String getCode() {
        return code;
    }
    public String getDisplayName() {
        return displayName;
    }

    public PublishLocale(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublishLocale)) return false;

        PublishLocale publishLocale = (PublishLocale) o;

        if (code != null ? !code.equals(publishLocale.code) : publishLocale.code != null) return false;
        if (displayName != null ? !displayName.equals(publishLocale.displayName) : publishLocale.displayName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "code: " + code + '\n' +
                "displayName: " + displayName;
    }
}