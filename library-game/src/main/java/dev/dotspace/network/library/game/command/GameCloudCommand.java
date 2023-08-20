package dev.dotspace.network.library.game.command;

import cloud.commandframework.CommandManager;
import org.jetbrains.annotations.NotNull;

/**
 * Handle command.
 *
 * @param <SENDER> generic type of sender.
 */
public interface GameCloudCommand<SENDER> {
    /**
     * Configure commands.
     * Will be called if command was initialized.
     *
     * @param manager same value as {@link GameCloudCommand#manager()}
     */
    void configure(@NotNull final CommandManager<SENDER> manager);

    /**
     * Get command manager of class.
     *
     * @return manager
     */
    @NotNull CommandManager<SENDER> manager();
}
