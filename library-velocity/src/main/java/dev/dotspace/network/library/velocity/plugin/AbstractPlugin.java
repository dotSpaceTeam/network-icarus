package dev.dotspace.network.library.velocity.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dotspace.network.library.game.plugin.AbstractGamePlugin;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.velocity.command.AbstractCommand;
import dev.dotspace.network.library.velocity.event.AbstractListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;


@Log4j2
@Getter
@Accessors(fluent=true)
public abstract class AbstractPlugin extends AbstractGamePlugin<Player, AbstractListener, AbstractCommand> {
  @Inject
  private ProxyServer server;
  @Inject
  private Logger logger;

  public AbstractPlugin() {
    this.module(new PluginModule(this));
    this.configure();

    //Create listener register.
    this.hookEventListener(AbstractListener.class, abstractListener -> {
      this.server.getEventManager().register(this, abstractListener);
    });

    //Create command register.
    this.hookCommand(AbstractCommand.class);

    this.executeRunnable(PluginState.PRE_LOAD);
    //--- Code start ---

    //--- Code end ---
    this.executeRunnable(PluginState.POST_LOAD);
  }

  @Subscribe
  public void handleEnable(@NotNull final ProxyInitializeEvent event) {
    this.lock();

    this.executeRunnable(PluginState.PRE_ENABLE);
    //--- Code start ---

    //Name of plugin
    final String name = this.server.getPluginManager()
        .fromInstance(this)
        .flatMap(pluginContainer -> pluginContainer.getDescription().getName())
        .orElse("Not defined.");

    this.init(name);

    //--- Code end ---
    this.executeRunnable(PluginState.POST_ENABLE);
  }

  @Subscribe
  public void handleShutdown(@NotNull final ProxyShutdownEvent event) {
    this.executeRunnable(PluginState.PRE_DISABLE);
    //--- Code start ---

    this.shutdown();

    //--- Code end ---
    this.executeRunnable(PluginState.POST_DISABLE);
  }

  /**
   * Get name of plugin class.
   *
   * @return class name.
   */
  public @NotNull String name() {
    return this.getClass().getName();
  }

  /**
   * See {@link AbstractGamePlugin#playerLocale(Object)}.
   */
  @Override
  protected @NotNull Locale playerLocale(@Nullable Player player) {
    return Optional.ofNullable(player)
        //Get player locale.
        .map(Player::getEffectiveLocale)
        //If player or player locale is null, use default.
        .orElse(Locale.getDefault());
  }
}
