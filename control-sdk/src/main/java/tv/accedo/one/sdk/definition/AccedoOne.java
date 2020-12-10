package tv.accedo.one.sdk.definition;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public interface AccedoOne {
    /**
     * @return the url of the Accedo One server used. Normally shouldn't be null.
     */
    String getEndpoint();

    /**
     * @return the deviceId sent to Accedo One. Normally shouldn't be null.
     */
    String getDeviceId();

    /**
     * @return an optional global identifier used for whitelisting. Can be null.
     */
    String getGid();

    /**
     * @return the appKey this service connects to. Normally shouldn't be null.
     */
    String getAppKey();

    /**
     * @return the serverTime, synced from the session call.
     */
    long getServerTime();


    /**
     * @return a service used to access assets and metedata from Accedo One.
     */
    AccedoOneControl control();

    /**
     * @return a service used to access Publish entries.
     */
    AccedoOnePublish publish();

    /**
     * @return a service used to send Event logs to Accedo One.
     */
    AccedoOneDetect detect();

    /**
     * @return a service used to send Application logs to Accedo One.
     */
    AccedoOneInsight insight();

    /**
     * @return a service used to access user specific data stored in Accedo One.
     */
    AccedoOneUserData userData();

    /**
     * @return a service used to access already prefetched assets and metadata in a safe synchronous way from the main thread, without even trying to access the network.
     */
    AccedoOneCache cache();
}