package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.provider.Provider;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
@Log4j2
public abstract class AbstractPlugin extends JavaPlugin implements IPlugin {

  private @Nullable Client client;

  @Override
  public final void onLoad() {
    log.info("Loading spring...");
    this.client = new Client(AbstractPlugin.class, new String[]{});
    //Pass load
    this.load();
  }

  @Override
  public final void onEnable() {
    //Pass enable
    this.enable();
  }

  @Override
  public final void onDisable() {
    this.client = null;
    //Pass disable
    this.disable();
  }

  @Override
  public @NotNull Client client() {
    //Return client.
    return Objects.requireNonNull(this.client);
  }

  @Override
  public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
    return Optional.empty();
  }
}
