package com.aayushatharva.cloudaddr.core;

import inet.ipaddr.IPAddressString;

import java.util.Comparator;

public final class IPAddressComparator implements Comparator<String> {

    public static final IPAddressComparator INSTANCE = new IPAddressComparator();

    @Override
    public int compare(String ipv6Address1, String ipv6Address2) {
        return new IPAddressString(ipv6Address1).compareTo(new IPAddressString(ipv6Address2));
    }

    private IPAddressComparator() {
        // Prevent outside initialization
    }
}
