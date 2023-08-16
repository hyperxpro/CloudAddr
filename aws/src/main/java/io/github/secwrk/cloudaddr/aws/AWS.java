package io.github.secwrk.cloudaddr.aws;

import java.util.List;

public record AWS(long syncToken, String createDate, List<PrefixIpv4> prefixes, List<PrefixIpv6> ipv6_prefixes) {

    public record PrefixIpv4(String ip_prefix, String region, String service, String network_border_group) {
        // NO-OP
    }

    public record PrefixIpv6(String ipv6_prefix, String region, String service, String network_border_group) {
        // NO-OP
    }
}
