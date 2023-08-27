package dev.dotspace.network.library;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


public final class Library {
  /**
   * Library module.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull LibraryModule module = new LibraryModule();
}
