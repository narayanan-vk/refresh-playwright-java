package com.neudesic.qa.configs;

import org.junit.platform.engine.FilterResult;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.launcher.PostDiscoveryFilter;

public class ShardingFilter implements PostDiscoveryFilter {
    private final int shardIndex;
    private final int shardTotal;

    public ShardingFilter() {
        this.shardIndex = Integer.parseInt(System.getProperty("shardIndex", "0"));
        this.shardTotal = Integer.parseInt(System.getProperty("shardTotal", "1"));
    }

    @Override
    public FilterResult apply(TestDescriptor object) {
        if (!object.isTest()) {
            return FilterResult.included("Not a test method");
        }
        int testHash = Math.abs(object.getUniqueId().hashCode());
        boolean include = (testHash % shardTotal) == shardIndex;
        return include ?
                FilterResult.included("Test included in shard " + shardIndex) :
                FilterResult.excluded("Test excluded from shard " + shardIndex);

    }
}
