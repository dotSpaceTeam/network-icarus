package dev.dotspace.network.library.spigot.self.command;


import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import dev.dotspace.network.library.game.message.context.ContextType;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.jvm.IHardwareInfo;
import dev.dotspace.network.library.jvm.IResourceInfo;
import dev.dotspace.network.library.jvm.ImmutableHardwareInfo;
import dev.dotspace.network.library.jvm.ImmutableResourceInfo;
import dev.dotspace.network.library.spigot.command.AbstractCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;


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

          final IResourceInfo resourceInfo = this.plugin().resourceInfo();
          //Get tps of server
          final double[] tps = Bukkit.getTPS();

          this.plugin()
              .message(player, ContextType.KEY, "icarus@spigot@tps")
              //Memory
              .replace("memory.used", resourceInfo.usedMemory())
              .replace("memory.total", resourceInfo.totalMemory())
              .replace("memory.use", String.format("%.2f", resourceInfo.memoryUsage()*100))

              //Cpu
              .replace("cpu.use", String.format("%.2f", resourceInfo.processorUsage()*100))
              .replace("cpu.cores", resourceInfo.cores())
              .replace("cpu.threads", resourceInfo.threads())

              //Tps and Server
              .replace("tps.1", String.format("%.4f", tps[0]))
              .replace("tps.5", String.format("%.4f", tps[1]))
              .replace("tps.15", String.format("%.4f", tps[2]))
              .replace("server.players", Bukkit.getOnlinePlayers().size())

              //Client
              .replace("", "")

              .handle(s -> {
                player.sendMessage(MiniMessage.miniMessage().deserialize(s));
              })
              .complete();
        }));
  }
}
