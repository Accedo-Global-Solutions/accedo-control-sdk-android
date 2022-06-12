package tv.accedo.one.sdk.implementation.utils;

import androidx.annotation.NonNull;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Dns;

public class DnsSelector implements Dns {

    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String hostname) throws UnknownHostException {
        // Prioritizing Inet4Addresses
        return Dns.SYSTEM.lookup(hostname).stream().sorted(Comparator.comparing(inetAddress ->
                inetAddress instanceof Inet6Address)).collect(Collectors.toList());
    }
}
