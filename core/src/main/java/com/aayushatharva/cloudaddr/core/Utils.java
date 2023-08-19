package com.aayushatharva.cloudaddr.core;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    /**
     * Regex pattern for IPv4 CIDR
     */
    public static final Pattern IPV4_PATTERN_CIDR = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(3[0-2]|[1-2]?[0-9])$");

    /**
     * Regex pattern for IPv4 CIDR in RFC 1918
     */
    public static final Pattern IPV4_PATTERN_CIDR_RFC_1918 = Pattern.compile("(^10\\.)|(^192\\.168\\.)|(^172\\.(1[6-9]|2[0-9]|3[0-1])\\.)");

    /**
     * Regex pattern for IPv4
     */
    public static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    /**
     * Check if the given IP address is a IPv4 belongs to RFC 1918 CIDR
     */
    public static boolean isRFC1918(String ipAddress) {
        Matcher matcher = IPV4_PATTERN_CIDR_RFC_1918.matcher(ipAddress);
        return matcher.find();
    }

    /**
     * Generate CIDR from start and end IP address
     *
     * @param start Start IP address
     * @param end   End IP address
     * @return {@link String} array of CIDR
     */
    public static String[] generateCidr(String start, String end) {
        IPAddressString startRange = new IPAddressString(start);
        IPAddressString endRange = new IPAddressString(end);

        IPAddress one = startRange.getAddress(), two = endRange.getAddress();
        IPAddressSeqRange range = one.spanWithRange(two);
        IPAddress[] blocks = range.spanWithPrefixBlocks();

        String[] cidrs = new String[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            cidrs[i] = blocks[i].toPrefixLengthString();
        }
        return cidrs;
    }
}
