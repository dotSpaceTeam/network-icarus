package dev.dotspace.network.library.game.command;

import cloud.commandframework.CommandManager;
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
public abstract class AbstractCloudCommand<SENDER> implements GameCloudCommand<SENDER> {
  /**
   * Variable for {@link GameCloudCommand#manager()}
   */
  private @Nullable CommandManager<SENDER> manager;

  @Override
  public @NotNull CommandManager<SENDER> manager() {
    return Objects.requireNonNull(this.manager);
  }
}
