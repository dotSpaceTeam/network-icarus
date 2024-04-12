package dev.dotspace.network.node.benchmark;

import dev.dotspace.common.function.ThrowableRunnable;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Log4j2
@Accessors(fluent=true)
public final class NodeBenchmark implements INodeBenchmark {
  private final @NotNull UUID benchmarkId;
  @Getter
  private boolean running;
  private final @NotNull List<IBenchmark> benchmarkList;

  public NodeBenchmark() {
    this.benchmarkList = new ArrayList<>();
    this.benchmarkId = UUID.randomUUID();

    log.info("Created node-benchmark={}.", this.benchmarkId);
  }

  @Override
  public @NotNull INodeBenchmark addBenchmark(@Nullable String caseName,
                                              @Nullable ThrowableRunnable caseRunnable) throws IllegalAccessException {
    if (this.running) {
      throw new IllegalAccessException("Already running.");
    }
    this.benchmarkList.add(new ImmutableBenchmark(caseName, caseRunnable));
    log.info("Added benchmark={}", caseName);
    return this;
  }

  @Override
  public @NotNull INodeBenchmark run() throws IllegalAccessException {
    if (this.running) {
      throw new IllegalAccessException("Already running.");
    }

    final Map<IBenchmark, Long> timestamps = new HashMap<>();
    log.info("Running node-benchmark={}.", this.benchmarkId);

    for (final IBenchmark benchmark : this.benchmarkList) {
      //Get start
      final long start = System.currentTimeMillis();

      log.info("Starting benchmark={}...", benchmark.caseName());

      try {
        benchmark.caseRunnable().run();
      } catch (Throwable e) {

      }

      //Get end
      final long duration = System.currentTimeMillis()-start;

      log.info("Benchmark={}, done took {}s.", benchmark.caseName(), String.format("%.3f", duration/1000.0));

      timestamps.put(benchmark, duration);
    }
    log.info("Node-benchmark={} done.", this.benchmarkId);
    final long total = timestamps.values().stream().mapToLong(aLong -> aLong).sum();
    log.info("Took a total of {}s.", String.format("%.3f", total/1000.0));

    return this;
  }
}
