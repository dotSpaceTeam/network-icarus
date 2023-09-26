package dev.dotspace.network.library.game.command;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import dev.dotspace.network.library.game.event.GameListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Abstract implementation of GameCloudCommand.
 *
 * @param <SENDER> generic type of sender.
 */
@Accessors(fluent=true)
public abstract class AbstractCloudCommand<PLUGIN, SENDER> implements GameCloudCommand<SENDER> {
  /**
   * Variable for {@link GameCloudCommand#manager()}
   */
  @Inject
  private @Nullable CommandManager<SENDER> manager;

  /**
   * Get trough {@link GameListener#plugin()}
   */
  @Inject
  @Getter
  private PLUGIN plugin;

  @Override
  public @NotNull CommandManager<SENDER> manager() {
    return Objects.requireNonNull(this.manager);
  }
}
