package dev.dotspace.network.library.jvm;

/**
 * Info of current system, software running on.
 */
public interface IHardwareInfo {
  /**
   * Get the amount of total memory in mb.
   *
   * @return amount of memory.
   */
  long memory();

  /**
   * Get the amount of used cores by jvm.
   *
   * @return amount of cores.
   */
  int cores();
}