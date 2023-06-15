package dev.dotspace.network.library.server;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface IRuntimeInfo {
  /**
   * Timestamp of info.
   *
   * @return date.
   */
  @NotNull Date timestamp();

  /**
   * Get the amount of total memory in mb.
   *
   * @return amount of memory.
   */
  long totalMemory();

  /**
   * Get the amount of unused memory in mb.
   *
   * @return amount of unused memory.
   */
  long unusedMemory();

  /**
   * Get the amount of used memory in mb.
   *
   * @return amount of used memory.
   */
  long usedMemory();

  /**
   * Get the amount of used cores by jvm.
   *
   * @return amount of cores.
   */
  int cores();

  /**
   * Get the amount of active threads.
   *
   * @return amount of threads.
   */
  int threads();

  /**
   * Get the value of processor usage.
   *
   * @return value of usage.
   */
  float processorUsage();

  /**
   * Get the value of one core processor usage.
   *
   * @return value of usage.
   */
  float singleCoreUsage();

  /**
   * Get the value of memory usage.
   *
   * @return value of usage.
   */
  float memoryUsage();
}