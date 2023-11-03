package dev.dotspace.network.library.spigot.self.command;


import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import dev.dotspace.network.client.RestClient;
import dev.dotspace.network.library.game.message.context.ContextType;
import dev.dotspace.network.library.spigot.command.AbstractCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


//Get tick information.
public final class PlaytimeCommand extends AbstractCommand {
  /**
   * Configure tps command
   */
  @Override
  public void configure(@NotNull CommandManager<CommandSender> manager) {
    manager.command(manager
        //Command info.
        .commandBuilder(
            "playtime",
            ArgumentDescription.of("Get playtime."),
            "pt")
        //Define sender must be player.
        .senderType(Player.class)
        //Handle command
        .handler(commandContext -> {
          //Sender
          final Player player = (Player) commandContext.getSender();

          this.plugin()
              .message(player, ContextType.KEY, "icarus@spigot@playtime");

          RestClient
              .client()
              .profileRequest()
              .getPlaytime(player.getUniqueId().toString())
              .ifPresent(iPlaytime -> {
                //Ignore if playtime is null.
                if (iPlaytime == null) {
                  return;
                }


                //Playtime itself.
                final Time time = Time.of(iPlaytime.duration());

                this.plugin()
                    .message(player, ContextType.KEY, "icarus@spigot@playtime")
                    .replace("time.hour", time.hours())
                    .replace("time.minute", time.minutes())
                    .handle(Component.class, player::sendMessage)
                    .complete();
              });
        }));

    manager.command(
        manager
            .commandBuilder(
                "playtime",
                ArgumentDescription.of("Get playtime."),
                "pt")
            .senderType(Player.class)
            .argument(StringArgument.of("name"))
            .handler(commandContext -> {
              //Sender
              final Player player = (Player) commandContext.getSender();
              //Get input name of player.
              final String name = commandContext.get("name");
              player.sendMessage("Other "+name);
            }));
  }

  private record Time(long hours,
                      long minutes
  ) {

    private static @NotNull Time of(final long mills) {
      final long seconds = mills/1000;
      final long minutes = seconds/60;
      final long hours = minutes/60;

      return new Time(hours, minutes);
    }
  }
}
