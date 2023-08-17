package com.aayushatharva.cloudaddr.core;

import java.util.Comparator;

public final class IPv6AddressComparator implements Comparator<String> {

    public static final IPv6AddressComparator INSTANCE = new IPv6AddressComparator();

    @Override
    public int compare(String ipv6Address1, String ipv6Address2) {
        String[] parts1 = ipv6Address1.split("/");
        String[] parts2 = ipv6Address2.split("/");

        String address1 = parts1[0];
        String address2 = parts2[0];

        int result = compareAddresses(address1, address2);

        if (result != 0) {
            return result;
        }

        if (parts1.length == 2 && parts2.length == 2) {
            return Integer.compare(Integer.parseInt(parts1[1]), Integer.parseInt(parts2[1]));
        } else if (parts1.length == 2) {
            return -1;
        } else if (parts2.length == 2) {
            return 1;
        }

        return 0;
    }

    private int compareAddresses(String address1, String address2) {
        String[] parts1 = address1.split(":");
        String[] parts2 = address2.split(":");

        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            if (!parts1[i].equals(parts2[i])) {
                return Integer.compare(Integer.parseInt(parts1[i], 16), Integer.parseInt(parts2[i], 16));
            }
        }

        return Integer.compare(parts1.length, parts2.length);
    }

    private IPv6AddressComparator() {
        // Prevent outside initialization
    }
}
