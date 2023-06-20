package dev.dotspace.network.library.server;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link IHardwareInfo}.
 *
 * @param memory see {@link IHardwareInfo#memory()}.
 * @param cores  see {@link IHardwareInfo#cores()}.
 */
public record ImmutableHardwareInfo(long memory,
                                    int cores) implements IHardwareInfo {

  //static

  /**
   * Get information of system this method is executed.
   *
   * @return instance of {@link IHardwareInfo}.
   */
  public static @NotNull IHardwareInfo get() {
    //Todo: memory to mb.
    final Runtime runtime = Runtime.getRuntime();
    return new ImmutableHardwareInfo(runtime.totalMemory(), runtime.availableProcessors());
  }
}