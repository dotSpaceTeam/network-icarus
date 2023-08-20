package dev.dotspace.network.library.spigot.self.command;


import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import dev.dotspace.network.library.spigot.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

//Get tick information.
public final class TickCommand extends AbstractCommand {
    /**
     * Configure tps command
     */
    @Override
    public void configure(@NotNull CommandManager<CommandSender> manager) {
        manager.command(manager
                //Command info.
                .commandBuilder(
                        "tps",
                        ArgumentDescription.of("Return server info."),
                        "tickspersecond",
                        "resources")
                //Permission
                .permission("icarus.tps")
                //Define sender must be player.
                .senderType(Player.class)
                //Handle command
                .handler(commandContext -> {
                    final Player player = (Player) commandContext.getSender(); //Sender

                    //Todo: Stuff
                    player.sendMessage("Resources.");
                }));
    }
}
