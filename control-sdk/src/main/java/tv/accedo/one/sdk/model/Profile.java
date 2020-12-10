package tv.accedo.one.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class Profile implements Serializable{
    private String profileId;
    private String profileName;
    private String profileDescription;
    private String profileLastModified;
    private String abTestCycleId;
    private String abTestCycleName;

    /**
     * @return the ID of the profile.
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * @return the name of the profile.
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @return the profile's description.
     */
    public String getProfileDescription() {
        return profileDescription;
    }

    /**
     * @return the date when the profile was last modified in UTC format.
     */
    public String getProfileLastModified() {
        return profileLastModified;
    }

    /**
     * @return (If the device is a part of an active A/B test) this attribute is the ID of the current A/B test cycle.
     */
    public String getAbTestCycleId() {
        return abTestCycleId;
    }

    /**
     * @return (If the device is a part of an active A/B test) The ID of the current A/B test cycle.
     */
    public String getAbTestCycleName() {
        return abTestCycleName;
    }

    @Override
    public String toString() {
        return "profileId: " + profileId + '\n' +
                "profileName: " + profileName + '\n' +
                "profileDescription: " + profileDescription + '\n' +
                "profileLastModified: " + profileLastModified + '\n' +
                "abTestCycleId: " + abTestCycleId + '\n' +
                "abTestCycleName: " + abTestCycleName;
    }

    public static Profile fromJson(JSONObject jsonObject) throws JSONException {
        Profile profile = new Profile();
        profile.profileId           = jsonObject.getString("profileId");
        profile.profileName         = jsonObject.optString("profileName", null);
        profile.profileDescription  = jsonObject.optString("profileDescription", null);
        profile.profileLastModified = jsonObject.optString("profileLastModified", null);
        profile.abTestCycleId       = jsonObject.optString("abTestCycleId", null);
        profile.abTestCycleName     = jsonObject.optString("abTestCycleName", null);
        return profile;
    }
}