package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.library.provider.Provider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractCorePlugin extends JavaPlugin implements IPlugin {

  @Override
  public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
    return Optional.empty();
  }
}
