package io.github.secwrk.cloudaddr.azure;

import java.util.List;

public record Azure(long changeNumber, String cloud, List<Value> values) {

    public record Value(String name, String id, Property properties) {

        public record Property(long changeNumber, String region, long regionId, String platform, String systemService,
                               List<String> addressPrefixes, List<String> networkFeatures) {
            // NO-OP
        }
    }
}
