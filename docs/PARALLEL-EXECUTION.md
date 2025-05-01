# Test Parallelization with GitHub Actions

## Overview

This project implements test parallelization using GitHub Actions matrix strategy and a custom JUnit sharding filter. This approach allows us to distribute our test execution across multiple parallel runners, significantly reducing the total execution time as our test suite grows.

## How It Works

The parallelization system has two main components:

### 1. Sharding Filter (Java)

The `ShardingFilter` class implements JUnit's `PostDiscoveryFilter` to divide tests across multiple execution shards:

```java
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
```

This filter:
- Takes two system properties: `shardIndex` (which shard this is) and `shardTotal` (total number of shards)
- Uses a hash-based distribution to assign tests to shards consistently
- Includes only the tests that belong to the current shard

### 2. GitHub Actions Workflow

The workflow file uses GitHub Actions' matrix strategy to run multiple jobs in parallel:

```yaml
jobs:
  run-playwright-tests:
    name: 'Playwright Tests (Shard ${{ matrix.shardIndex }} of ${{ matrix.shardTotal }})'
    runs-on: ubuntu-latest
    strategy:
      fail-fast: false
      matrix:
        shardIndex: [0, 1, 2, 3]
        shardTotal: [4]
```

Key workflow features:
- Spawns multiple parallel containers, each running a different test shard
- Passes the shard information to Maven via system properties
- Collects and merges test results from all shards
- Publishes a single consolidated report

## Benefits

- **Scalability**: As our test suite grows to thousands of tests, parallelization will significantly reduce overall execution time
- **Consistent Distribution**: Hash-based approach ensures tests are distributed evenly and consistently across shards
- **Flexible Configuration**: Shard count can be adjusted via workflow inputs to balance execution time versus resource usage

## Usage

When running a workflow:
1. GitHub Actions creates multiple parallel jobs based on the matrix configuration
2. Each job runs with a specific `shardIndex` (0, 1, 2, etc.) and `shardTotal` (total number of shards)
3. The Maven command passes these values to the JUnit filter via system properties
4. Each shard runs only its assigned subset of tests
5. Results are merged after all shards complete

No additional configuration is needed - just run the workflow normally, and tests will automatically be distributed across the configured number of shards.
