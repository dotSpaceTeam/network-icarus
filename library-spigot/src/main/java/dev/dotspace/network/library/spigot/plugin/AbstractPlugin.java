package dev.dotspace.network.library.spigot.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import dev.dotspace.network.library.game.plugin.GamePlugin;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.provider.Provider;
import dev.dotspace.network.library.spigot.LibraryModule;
import dev.dotspace.network.library.spigot.event.AbstractListener;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


@Log4j2
public abstract class AbstractPlugin extends JavaPlugin implements GamePlugin {
  /**
   * Plugin to handle system, cant be extended because {@link JavaPlugin} needed by default.
   */
  private final @NotNull SpigotPlugin spigotPlugin = new SpigotPlugin();

  /**
   * Pass {@link SpigotPlugin#injector()}
   */
  @Override
  public @NotNull Injector injector() {
    return this.spigotPlugin.injector();
  }

  /**
   * Pass {@link SpigotPlugin#provider(Class)}
   */
  @Override
  public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
    return this.spigotPlugin.provider(providerClass);
  }

  /**
   * Pass {@link SpigotPlugin#handle(PluginState, Runnable)}
   */
  @Override
  public @NotNull GamePlugin handle(@Nullable PluginState state,
                                    @Nullable Runnable runnable) {
    return this.spigotPlugin.handle(state, runnable);
  }

  /**
   * Pass {@link SpigotPlugin#module(AbstractModule)}
   */
  @Override
  public @NotNull <MODULE extends AbstractModule> GamePlugin module(@Nullable MODULE module) {
    return this.spigotPlugin.module(module);
  }

  @Override
  public final void onLoad() {
    //Configure.
    this.module(new PluginModule(this));
    this.module(LibraryModule.instance());

    //Hoo spigot events
    this.spigotPlugin.hookEventListener(AbstractListener.class, abstractListener -> {
      this.getServer().getPluginManager().registerEvents(abstractListener, this);
    });

    this.configure();

    this.spigotPlugin.executeRunnable(PluginState.PRE_LOAD);
    //--- Code start ---

    //--- Code end ---
    this.spigotPlugin.executeRunnable(PluginState.POST_LOAD);
  }

  @Override
  public final void onEnable() {
    //Lock and finalize system.
    this.spigotPlugin.lock();

    this.spigotPlugin.executeRunnable(PluginState.PRE_ENABLE);
    //--- Code start ---

    this.spigotPlugin.init(this.getName());

    //--- Code end ---
    this.spigotPlugin.executeRunnable(PluginState.POST_ENABLE);
  }

  @Override
  public final void onDisable() {
    this.spigotPlugin.executeRunnable(PluginState.PRE_DISABLE);
    //--- Code start ---

    this.spigotPlugin.shutdown();

    //--- Code end ---
    this.spigotPlugin.executeRunnable(PluginState.POST_DISABLE);
  }


}
