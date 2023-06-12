package dev.dotspace.network.library.key;

import org.jetbrains.annotations.NotNull;

/**
 * Apply an key to an object.
 */
public interface IKey {
  /**
   * Key of object as string.
   *
   * @return ey as string.
   */
  @NotNull String key();
}
