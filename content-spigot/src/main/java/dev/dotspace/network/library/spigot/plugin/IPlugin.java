package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IPlugin {
  /**
   * Run on load
   */
  void load();

  /**
   * Run on enable
   */
  void enable();

  /**
   * Run on disable
   */
  void disable();

  /**
   * Get client
   *
   * @return client of spring.
   */
  @NotNull Client client();

  /**
   * Get provider.
   *
   * @param providerClass class instance of provider.
   * @param <PROVIDER>    generic type of provider.
   * @return provider.
   */
  @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable final Class<PROVIDER> providerClass);
}
