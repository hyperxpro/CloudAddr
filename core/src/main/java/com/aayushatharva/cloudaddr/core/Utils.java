package com.aayushatharva.cloudaddr.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    /**
     * Regex pattern for IPv4 CIDR
     */
    public static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(3[0-2]|[1-2]?[0-9])$");

    public static boolean isRFC1918(String ipAddress) {
        String privateIPPattern =
                "(^10\\.)|(^192\\.168\\.)|(^172\\.(1[6-9]|2[0-9]|3[0-1])\\.)";

        Pattern pattern = Pattern.compile(privateIPPattern);
        Matcher matcher = pattern.matcher(ipAddress);

        return matcher.find();
    }
}
