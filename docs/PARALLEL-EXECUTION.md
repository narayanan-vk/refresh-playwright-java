# Test Parallelization with GitHub Actions

## Overview

This project implements test parallelization using GitHub Actions matrix strategy and JUnit 5's parallel execution capabilities and a custom JUnit sharding filter. This approach allows us to distribute our test execution across multiple parallel runners and run tests concurrently within each runner, significantly reducing the total execution time as our test suite grows.

## How It Works

The parallelization system has three main components:

### 1. JUnit 5 Parallel Execution Configuration

JUnit 5 provides built-in support for parallel test execution through properties set in `junit-platform.properties`:

```properties
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.mode.default = concurrent
junit.jupiter.execution.parallel.mode.classes.default = same_thread
junit.jupiter.execution.parallel.config.strategy = dynamic
junit.jupiter.execution.parallel.config.dynamic.factor = 0.5
```

These settings:

- `parallel.enabled = true`: Enables parallel execution of tests
- `parallel.mode.default = concurrent`: Runs test methods within a class concurrently
- `parallel.mode.classes.default = same_thread`: Keeps test methods from the same class on the same thread (prevents class-level concurrency issues)
- `config.strategy = dynamic`: Automatically determines parallelism level based on available processors
- `config.dynamic.factor = 0.5`: Uses 50% of available processors for test execution

These settings can be adjusted based on your needs:
- For heavier tests, you might reduce the `dynamic.factor` to 0.25
- To run classes in parallel, change `mode.classes.default` to `concurrent`
- For completely sequential execution within a shard, set `parallel.enabled` to `false`

### 2. Sharding Filter (Java)

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

### 3. GitHub Actions Workflow

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

## Test Independence Requirements

For successful parallel execution, tests must be independent:

1. **Avoid shared state**: Tests should not depend on state created by other tests
2. **Isolate resources**: Each test should use its own test data and resources
3. **No order dependencies**: Tests should not rely on executing in a specific order
4. **Thread safety**: Any shared utilities or helpers must be thread-safe
5. **Independent setup/teardown**: Each test should handle its own setup and cleanup

## Benefits

- **Two-level parallelization**: Divides tests across machines AND runs tests concurrently within each machine
- **Scalability**: As our test suite grows to thousands of tests, parallelization will significantly reduce overall execution time
- **Consistent Distribution**: Hash-based approach ensures tests are distributed evenly and consistently across shards
- **Flexible Configuration**: Both shard count and JUnit parallel settings can be adjusted to balance execution time versus resource usage
- **Resource Efficiency**: Dynamic factor for JUnit parallelism prevents overloading individual machines

## Usage

When running a workflow:
1. GitHub Actions creates multiple parallel jobs based on the matrix configuration
2. Each job runs with a specific `shardIndex` (0, 1, 2, etc.) and `shardTotal` (total number of shards)
3. The Maven command passes these values to the JUnit filter via system properties
4. Each shard runs only its assigned subset of tests, with those tests running in parallel according to JUnit settings
5. Results are merged after all shards complete

No additional configuration is needed - just run the workflow normally, and tests will automatically be distributed across the configured number of shards.

