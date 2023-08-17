package com.aayushatharva.cloudaddr.core;

import java.util.Comparator;

public final class IPv6AddressComparator implements Comparator<String> {

    public static final IPv6AddressComparator INSTANCE = new IPv6AddressComparator();

    @Override
    public int compare(String ip1, String ip2) {
        String[] parts1 = ip1.split("/");
        String[] parts2 = ip2.split("/");

        String ipPart1 = parts1[0];
        String ipPart2 = parts2[0];

        int ipComparison = compareIPv6(ipPart1, ipPart2);

        if (ipComparison != 0) {
            return ipComparison;
        } else {
            // If IP addresses are equal, compare CIDR notations
            if (parts1.length == 2 && parts2.length == 2) {
                int cidr1 = Integer.parseInt(parts1[1]);
                int cidr2 = Integer.parseInt(parts2[1]);
                return Integer.compare(cidr1, cidr2);
            } else {
                // Handle case where one is a full IP and the other is a CIDR
                return Integer.compare(parts1.length, parts2.length);
            }
        }
    }

    private int compareIPv6(String ip1, String ip2) {
        // Implement IPv6 comparison logic here
        // You can use libraries like `InetAddress` to convert and compare IPv6 addresses.
        // Return a negative value if ip1 < ip2, 0 if ip1 == ip2, and a positive value if ip1 > ip2.
        // For simplicity, let's assume ip1 and ip2 are valid IPv6 addresses or CIDR notations.
        // You can refine this part depending on your exact requirements.

        // Example comparison using String's natural order:
        return ip1.compareTo(ip2);
    }

    private IPv6AddressComparator() {
        // Prevent outside initialization
    }
}
