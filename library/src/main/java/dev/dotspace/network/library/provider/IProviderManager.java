package dev.dotspace.network.library.provider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IProviderManager {
  /**
   * Get a provider from class.
   *
   * @param providerClass to get provider from.
   * @param <PROVIDER>    generic type of provider.
   * @return potentially nullable provider.
   * @throws NullPointerException if providerClass is null.
   */
  @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable final Class<PROVIDER> providerClass);

  /**
   * Get a provider from class.
   *
   * @param providerClass to get provider from.
   * @param <PROVIDER>    generic type of provider.
   * @return potentially nullable provider.
   * @throws NullPointerException if provider is null.
   * @throws NullPointerException if providerClass is null.
   */
  @NotNull <PROVIDER extends Provider> PROVIDER providerElseThrow(@Nullable final Class<PROVIDER> providerClass) throws NullPointerException;

  /**
   * Update a provider.
   * Ignores operation if provider is null.
   *
   * @param provider   to update.
   * @param <PROVIDER> generic type of provider.
   * @return instance of manger.
   */
  @NotNull <PROVIDER extends Provider> IProviderManager provider(@Nullable final PROVIDER provider);
}
