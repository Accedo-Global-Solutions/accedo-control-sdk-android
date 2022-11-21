package tv.accedo.one.sdk.implementation.utils;

import androidx.annotation.NonNull;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import okhttp3.Dns;

/**
 * DNS selector for prioritizing IPV4 addresses over IPV6
 */
public class DnsSelector implements Dns {

    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String hostname) throws UnknownHostException {
        // Prioritizing Inet4Addresses
        List<InetAddress> addresses = Dns.SYSTEM.lookup(hostname);
        Collections.sort(addresses, (inetAddress1, inetAddress2) -> {
                    if (inetAddress1 instanceof Inet4Address && inetAddress2 instanceof Inet6Address) {
                        return -1;
                    }

                    if (inetAddress1 instanceof Inet6Address && inetAddress2 instanceof Inet4Address) {
                        return 1;
                    }

                    return 0;
                }

        );
        return addresses;
    }
}
