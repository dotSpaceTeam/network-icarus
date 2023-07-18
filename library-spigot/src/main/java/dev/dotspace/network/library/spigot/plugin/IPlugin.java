package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IPlugin {
  /**
   * Get provider.
   *
   * @param providerClass class instance of provider.
   * @param <PROVIDER>    generic type of provider.
   * @return provider.
   */
  @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable final Class<PROVIDER> providerClass);
}
