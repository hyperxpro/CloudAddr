package io.github.secwrk.cloudaddr.core;

import java.util.List;

public record Prefixes(String cloudProvider, long creationDate, List<String> prefixes) {

    public Prefixes(String cloudProvider, List<String> prefixes) {
        this(cloudProvider, System.currentTimeMillis(), prefixes);
    }
}
