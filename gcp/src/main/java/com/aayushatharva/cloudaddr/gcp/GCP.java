package com.aayushatharva.cloudaddr.gcp;

import java.util.List;

public record GCP (long syncToken, String creationTime, List<Prefix> prefixes) {

    public record Prefix(String ipv4Prefix, String ipv6Prefix, String service, String scope) {
        // NO-OP
    }
}
