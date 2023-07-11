package dev.dotspace.network.library.server;

final class MemoryCalculator {
  /**
   * Memory multiplication to mb.
   */
  private final static long MEMORY_INDEX = 1024 * 1024;

  /**
   * Convert mb to byte.
   *
   * @param value to convert.
   * @return converted byte.
   */
  public static long convertByteToMegabyte(final long value) {
    return value / MEMORY_INDEX;
  }
}
