package dev.dotspace.network.library;

import dev.dotspace.common.response.ResponseService;
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

  /**
   * Get {@link ResponseService} instance of the library module.
   *
   * @return same instance as {@link LibraryModule#responseService()}.
   */
  public static @NotNull ResponseService responseService() {
    return module.responseService();
  }
}
