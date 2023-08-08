package dev.dotspace.network.library.spigot.self;

import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import lombok.extern.log4j.Log4j2;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Log4j2
public final class IcarusPlugin extends AbstractPlugin {

    @Override
    public void configure() {
        this
                .handle(PluginState.PRE_ENABLE, () -> {
                    //Unregister unwanted commands.
                    this.unregisterCommand("tps");
                    this.unregisterCommand("reload");
                })

                //Enable client.
                .handle(PluginState.POST_ENABLE, Client::enable);
    }

    /**
     * Unregister command from server.
     *
     * @param commandString command to unregister.
     */
    public void unregisterCommand(@NotNull final String commandString) {
        //Set command to lowercase
        final String lowerCommand = commandString.toLowerCase(Locale.ROOT);
        //Get command from map.
        @Nullable final Command command = this.getServer().getCommandMap().getKnownCommands().remove(lowerCommand);

        //Return if command is not present.
        if (command == null) {
            return;
        }

        command.unregister(this.getServer().getCommandMap());
        log.info("Removed '{}' from command map.", lowerCommand);
    }
}
