package dev.dotspace.network.library.jvm;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Date;

import static dev.dotspace.network.library.jvm.MemoryCalculator.*;


/**
 * Implementation of {@link IResourceInfo}.
 *
 * @param timestamp      see {@link IResourceInfo#timestamp()}.
 * @param totalMemory    see {@link IResourceInfo#totalMemory()}.
 * @param unusedMemory   see {@link IResourceInfo#unusedMemory()}.
 * @param cores          see {@link IResourceInfo#cores()}.
 * @param threads        {@link IResourceInfo#threads()}.
 * @param processorUsage {@link IResourceInfo#processorUsage()}.
 */
public record ImmutableResourceInfo(@NotNull Date timestamp,
                                    long totalMemory,
                                    long unusedMemory,
                                    int cores,
                                    int threads,
                                    float processorUsage
) implements IResourceInfo {
  /**
   * See {@link IResourceInfo#usedMemory()}.
   */
  @Override
  public long usedMemory() {
    return this.totalMemory()-this.unusedMemory();
  }

  /**
   * See {@link IResourceInfo#singleCoreUsage()}.
   */
  @Override
  public float singleCoreUsage() {
    return this.processorUsage()/this.cores();
  }

  /**
   * See {@link IResourceInfo#memoryUsage()}.
   */
  @Override
  public float memoryUsage() {
    return (float) this.usedMemory()/(float) this.totalMemory();
  }

  //static
  /**
   * Get info of operating system.
   */
  private final static @NotNull OperatingSystemMXBean OPERATING_SYSTEM_MX_BEAN;

  static {
    OPERATING_SYSTEM_MX_BEAN = ManagementFactory.getOperatingSystemMXBean();
  }

  /**
   * Get information now of system this method is executed.
   *
   * @return instance of {@link IResourceInfo}.
   */
  public static @NotNull IResourceInfo now() {
    final Runtime runtime = Runtime.getRuntime();

    return new ImmutableResourceInfo(
        new Date(System.currentTimeMillis()),

        convertByteToMegabyte(runtime.totalMemory()),
        convertByteToMegabyte(runtime.freeMemory()),

        runtime.availableProcessors(),
        ManagementFactory.getThreadMXBean().getThreadCount(),
        Math.max(0f, (float) OPERATING_SYSTEM_MX_BEAN.getSystemLoadAverage())
    );
  }
}