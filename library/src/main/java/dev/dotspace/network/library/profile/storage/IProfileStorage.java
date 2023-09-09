package dev.dotspace.network.library.profile.storage;

import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import org.jetbrains.annotations.NotNull;


public interface IProfileStorage extends IProfileAttribute {
  /**
   * Get table of storage.
   *
   * @return table attributed is stored in.
   */
  @NotNull String table();
}
