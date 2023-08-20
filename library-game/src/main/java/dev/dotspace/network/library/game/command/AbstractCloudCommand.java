package dev.dotspace.network.library.game.command;

import cloud.commandframework.CommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Abstract implementation of GameCloudCommand.
 *
 * @param <SENDER> generic type of sender.
 */
@Accessors(fluent = true)
public abstract class AbstractCloudCommand<SENDER> implements GameCloudCommand<SENDER> {
    /**
     * Variable for {@link GameCloudCommand#manager()}
     */
    @Getter
    private CommandManager<SENDER> manager;
}
