package tv.accedo.one.sdk.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class OptionalParams implements Serializable {
	private boolean preview = false;
	private Date at;
    private String locale;

    /**
	 * If set to "true" the response will return the latest values for this Entry whether it is published or not. Default is "false"
	 * @return
	 */
	public boolean isPreview() {
		return preview;
	}

	/**
	 * Used to get Entry preview for specific moment of time in past or future. Can not be used if "preview" is set to "true".
	 * @return
	 */
	public Date getAt() {
		return at;
	}

    /**
     * Used for Publish content localization. If not null or empty, a locale=[locale] parameter will be
     * attached to every Publish request.
     * @return
     */
    public String getLocale() {
        return locale;
    }

	/**
	 * If set to "true" the response will return the latest values for this Entry whether it is published or not. Default is "false"
	 * @param preview
	 * @return
	 */
	public OptionalParams setPreview(boolean preview) {
		this.preview = preview;
		return this;
	}

	/**
	 * Used to get Entry preview for specific moment of time in past or future. Can not be used if "preview" is set to "true".
	 * @param at
	 * @return
	 */
	public OptionalParams setAt(Date at) {
		this.at = at;
		return this;
	}

    /**
     * Sets the locale for Publish content localization. If not null or empty, a locale=[locale] parameter will be
     * attached to every Publish request.
     * @param locale the locale to use
     * @return
     */
    public OptionalParams setLocale(String locale) {
        this.locale = locale;
        return this;
    }

	public OptionalParams() {
	}
    public OptionalParams(String locale) {
        this.locale = locale;
    }
    public OptionalParams(OptionalParams optionalParams) {
        if (optionalParams != null) {
            this.preview = optionalParams.preview;
            this.at = optionalParams.at;
            this.locale = optionalParams.locale;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionalParams)) return false;

        OptionalParams that = (OptionalParams) o;

        if (preview != that.preview) return false;
        if (at != null ? !at.equals(that.at) : that.at != null) return false;
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (preview ? 1 : 0);
        result = 31 * result + (at != null ? at.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        return result;
    }
}