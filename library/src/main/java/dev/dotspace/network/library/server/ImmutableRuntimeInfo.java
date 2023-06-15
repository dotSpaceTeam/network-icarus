package dev.dotspace.network.library.server;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Date;

/**
 * Implementation of {@link IRuntimeInfo}.
 *
 * @param timestamp      see {@link IRuntimeInfo#timestamp()}.
 * @param totalMemory    see {@link IRuntimeInfo#totalMemory()}.
 * @param unusedMemory   see {@link IRuntimeInfo#unusedMemory()}.
 * @param cores          see {@link IRuntimeInfo#cores()}.
 * @param threads        {@link IRuntimeInfo#threads()}.
 * @param processorUsage {@link IRuntimeInfo#processorUsage()}.
 */
public record ImmutableRuntimeInfo(@NotNull Date timestamp,
                                   long totalMemory,
                                   long unusedMemory,
                                   int cores,
                                   int threads,
                                   float processorUsage) implements IRuntimeInfo {
  /**
   * See {@link IRuntimeInfo#usedMemory()}.
   */
  @Override
  public long usedMemory() {
    return this.totalMemory() - this.unusedMemory();
  }

  /**
   * See {@link IRuntimeInfo#singleCoreUsage()}.
   */
  @Override
  public float singleCoreUsage() {
    return this.processorUsage() / this.cores();
  }

  /**
   * See {@link IRuntimeInfo#memoryUsage()}.
   */
  @Override
  public float memoryUsage() {
    return (float) this.usedMemory() / (float) this.totalMemory();
  }

  //static
  /**
   * Memory multiplication to mb.
   */
  private final static long MEMORY_INDEX;
  /**
   * Get info of operating system.
   */
  private final static @NotNull OperatingSystemMXBean OPERATING_SYSTEM_MX_BEAN;

  static {
    MEMORY_INDEX = 1024 * 1024;
    OPERATING_SYSTEM_MX_BEAN = ManagementFactory.getOperatingSystemMXBean();
  }

  /**
   * Get information now of system this method is executed.
   *
   * @return instance of {@link IRuntimeInfo}.
   */
  public static @NotNull IRuntimeInfo now() {
    final Runtime runtime = Runtime.getRuntime();

    return new ImmutableRuntimeInfo(
      new Date(System.currentTimeMillis()),
      runtime.totalMemory() / MEMORY_INDEX,
      runtime.freeMemory() / MEMORY_INDEX,
      runtime.availableProcessors(),
      ManagementFactory.getThreadMXBean().getThreadCount(),
      (float) OPERATING_SYSTEM_MX_BEAN.getSystemLoadAverage()
    );
  }
}