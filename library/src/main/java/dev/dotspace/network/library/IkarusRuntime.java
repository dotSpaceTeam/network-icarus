package dev.dotspace.network.library;

import dev.dotspace.network.library.provider.ProviderManager;
import org.jetbrains.annotations.NotNull;

public final class IkarusRuntime {

  private final static @NotNull ProviderManager providerManager = new ProviderManager();

  public static @NotNull ProviderManager providers() {
    return providerManager;
  }

}
