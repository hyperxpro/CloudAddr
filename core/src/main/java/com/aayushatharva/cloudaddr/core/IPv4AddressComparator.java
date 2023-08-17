package com.aayushatharva.cloudaddr.core;

import java.util.Comparator;

public final class IPv4AddressComparator implements Comparator<String> {

    public static final IPv4AddressComparator INSTANCE = new IPv4AddressComparator();

    @Override
    public int compare(String ipAddress1, String ipAddress2) {
        String[] parts1 = ipAddress1.split("/");
        String[] parts2 = ipAddress2.split("/");

        String ip1 = parts1[0];
        String ip2 = parts2[0];

        String[] octets1 = ip1.split("\\.");
        String[] octets2 = ip2.split("\\.");

        for (int i = 0; i < 4; i++) {
            int octet1 = Integer.parseInt(octets1[i]);
            int octet2 = Integer.parseInt(octets2[i]);

            if (octet1 < octet2) {
                return -1;
            } else if (octet1 > octet2) {
                return 1;
            }
        }

        if (parts1.length > 1 && parts2.length > 1) {
            int subnet1 = Integer.parseInt(parts1[1]);
            int subnet2 = Integer.parseInt(parts2[1]);

            if (subnet1 < subnet2) {
                return -1;
            } else if (subnet1 > subnet2) {
                return 1;
            }
        }

        return 0;
    }

    private IPv4AddressComparator() {
        // Prevent outside initialization
    }
}
