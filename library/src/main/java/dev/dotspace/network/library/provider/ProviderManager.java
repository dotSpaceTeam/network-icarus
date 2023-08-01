package dev.dotspace.network.library.provider;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public final class ProviderManager implements IProviderManager {
  private final Map<Class<?>, Object> map;

  public ProviderManager() {
    this.map = new ConcurrentHashMap<>();
  }


  /**
   * See {@link IProviderManager#provider(Provider)}
   */
  @Override
  public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
    return Optional.ofNullable(this.get(providerClass));
  }

  /**
   * See {@link IProviderManager#providerElseThrow(Class)}
   */
  @Override
  public <PROVIDER extends Provider> @NotNull PROVIDER providerElseThrow(@Nullable Class<PROVIDER> providerClass) throws NullPointerException {
    return Objects.requireNonNull(this.get(providerClass), "Provider of class={" + providerClass + "} not present.");
  }

  /**
   * Get a provider from class.
   */
  @SuppressWarnings("unchecked")
  private <PROVIDER extends Provider> @Nullable PROVIDER get(@Nullable final Class<PROVIDER> providerClass) {
    //Null check
    Objects.requireNonNull(providerClass);

    //Check cast
    try {
      //Get present or null.
      return (PROVIDER) this.map.get(providerClass);
    } catch (final ClassCastException exception) {
      //If error return null.
      log.warn("No present provider for class={} found. Maybe class does not match.", providerClass);
      return null;
    }
  }

  /**
   * See {@link IProviderManager#provider(Provider)}
   */
  @Override
  public @NotNull <PROVIDER extends Provider> IProviderManager provider(@Nullable PROVIDER provider) {
    //Only run if provider is present otherwise ignore operation.
    if (provider != null) {
      this.map.put(provider.getClass(), provider);
      log.info("Updated provider={}.", provider.getClass());
    }
    return this;
  }
}
