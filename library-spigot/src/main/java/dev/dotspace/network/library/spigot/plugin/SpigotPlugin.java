package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.game.plugin.AbstractGamePlugin;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.spigot.command.AbstractCommand;
import dev.dotspace.network.library.spigot.event.AbstractListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * Class exists, because {@link org.bukkit.plugin.java.JavaPlugin} needs to be extended.
 * Hacky class.
 */
final class SpigotPlugin extends AbstractGamePlugin<Player, AbstractListener, AbstractCommand> {

  /**
   * See {@link AbstractGamePlugin#executeRunnable(PluginState)}
   * There to be used in code.
   */
  @Override
  protected void executeRunnable(@Nullable PluginState state) {
    super.executeRunnable(state);
  }

  /**
   * See {@link AbstractGamePlugin#executeRunnable(PluginState)}
   * There to be used in code.
   */
  @Override
  protected @NotNull <TYPE> Stream<TYPE> createInstanceOf(@NotNull Reflections reflections,
                                                          @NotNull Class<TYPE> typeClass) {
    return super.createInstanceOf(reflections, typeClass);
  }

  /**
   * Ignore.
   */
  @Override
  public void configure() {
    /*
     * Ignore
     */
  }

  /**
   * See {@link AbstractGamePlugin#lock()}
   * There to be used in code.
   */
  @Override
  protected void lock() {
    super.lock();
  }

  /**
   * See {@link AbstractGamePlugin#init(String)}
   * There to be used in code.
   */
  @Override
  protected void init(@Nullable String name) {
    super.init(name);
  }

  /**
   * See {@link AbstractGamePlugin#shutdown()}
   * There to be used in code.
   */
  @Override
  protected synchronized void shutdown() {
    super.shutdown();
  }

  /**
   * See {@link AbstractGamePlugin#hookEventListener(Class, Consumer)}
   * There to be used in code.
   */
  @Override
  protected void hookEventListener(@Nullable Class<AbstractListener> abstractListenerClass,
                                   @Nullable Consumer<AbstractListener> abstractListenerConsumer) {
    super.hookEventListener(abstractListenerClass, abstractListenerConsumer);
  }

  /**
   * See {@link AbstractGamePlugin#hookCommand(Class)}
   * There to be used in code.
   */
  @Override
  protected void hookCommand(@Nullable Class<AbstractCommand> abstractCommandClass) {
    super.hookCommand(abstractCommandClass);
  }

  /**
   * See {@link AbstractGamePlugin#playerLocale(Object)}
   */
  @Override
  protected @NotNull Locale playerLocale(@Nullable Player player) {
    return player == null ? Locale.getDefault() : player.locale();
  }

  /**
   * See {@link AbstractGamePlugin#entryClass()}
   * There to be used in code.
   */
  @Override
  protected AbstractGamePlugin<Player, AbstractListener, AbstractCommand> entryClass(@Nullable Class<?> entryClass) {
    return super.entryClass(entryClass);
  }
}
