package dev.dotspace.network.node.benchmark;

import dev.dotspace.common.function.ThrowableRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Benchmark the client and server.
 */
public interface INodeBenchmark {
  /**
   * Add benchmark to object.
   *
   * @param caseName     to add case name.
   * @param caseRunnable task to execute.
   */
  @NotNull INodeBenchmark addBenchmark(@Nullable final String caseName,
                                       @Nullable final ThrowableRunnable caseRunnable) throws IllegalAccessException;

  /**
   * Run benchmark.
   */
  @NotNull INodeBenchmark run() throws IllegalAccessException;

  /**
   * Check if running.
   */
  boolean running();
}
