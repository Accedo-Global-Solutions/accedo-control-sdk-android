package tv.accedo.one.sdk.implementation.utils;

/**
 * Configurations to setup OkHttp client.
 */
public class NetworkConfiguration {
    protected static final long DEFAULT_TIMEOUT_CONNECT = 5000L;
    protected static final long DEFAULT_TIMEOUT_READ = 10000L;

    /**
     * Connection timeout in milliseconds
     */
    private final long connectionTimeout;

    /**
     * Connection timeout in milliseconds
     */
    private final long readTimeout;

    /**
     * Prioritizes ipv4 over ipv6
     * Default false
     */
    private final boolean forceIpv4;

    private NetworkConfiguration(long connectionTimeout, long readTimeout, boolean forceIpv4) {
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.forceIpv4 = forceIpv4;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public boolean isForceIpv4() {
        return forceIpv4;
    }

    /**
     * Builder class for Configuration
     */
    public static class Builder {

        private long connectionTimeout = DEFAULT_TIMEOUT_CONNECT;
        private long readTimeout = DEFAULT_TIMEOUT_READ;
        private boolean forceIpv4 = false;

        /**
         * Modifies the current connection timeout in Ms
         *
         * @param connectionTimeout API connection timeout in milliseconds
         */
        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Modifies the current read timeout in Ms
         *
         * @param readTimeout API response read timeout in milliseconds
         */
        public Builder setReadTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Forcing ipv4 over ipv6
         *
         * @param forceIpv4 Default is false
         */
        public Builder setForceIpv4(boolean forceIpv4) {
            this.forceIpv4 = forceIpv4;
            return this;
        }

        public NetworkConfiguration build() {
            return new NetworkConfiguration(connectionTimeout, readTimeout, forceIpv4);
        }
    }
}
