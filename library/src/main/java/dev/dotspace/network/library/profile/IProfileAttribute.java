package dev.dotspace.network.library.profile;

import dev.dotspace.network.library.key.IKey;
import org.jetbrains.annotations.NotNull;

/**
 * Also see {@link IKey}.
 */
public interface IProfileAttribute extends IKey {
  /**
   * Value of attribute.
   *
   * @return value of attribute.
   */
  @NotNull String value();
}
