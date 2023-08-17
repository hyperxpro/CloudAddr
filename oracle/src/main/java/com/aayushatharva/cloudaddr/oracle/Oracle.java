package com.aayushatharva.cloudaddr.oracle;

import java.util.List;

public record Oracle(String last_updated_timestamp, List<Region> regions) {

    public record Region(String region, List<CIDR> cidrs) {

        public record CIDR(String cidr, List<String> tags) {
            // NO-OP
        }
    }
}
