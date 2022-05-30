package tv.accedo.one.sdk.implementation;

import android.util.Log;

import androidx.annotation.NonNull;

import java.net.Inet4Address;
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
        Log.d("DNSDEBUG", "HOSTNAME: " + hostname);
        // If works, also test with prioritizing Inet4Addresses
//        Dns.SYSTEM.lookup(hostname).stream().sorted(Comparator.comparing(inetAddress -> inetAddress instanceof Inet6Address));
        List<InetAddress> ipv4Addresses = Dns.SYSTEM.lookup(hostname).stream().filter(inetAddress -> inetAddress instanceof Inet4Address).collect(Collectors.toList());
        ipv4Addresses.forEach(inetAddress -> {
            Log.d("DNSDEBUG", "inetAddress: " + inetAddress.getHostAddress());
        });
        return ipv4Addresses;
    }
}
