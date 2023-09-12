package dev.dotspace.network.library.spigot.self;

import dev.dotspace.network.client.Client;
import dev.dotspace.network.client.ClientState;
import dev.dotspace.network.library.game.permission.Permission;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import dev.dotspace.network.library.spigot.self.message.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Locale;


@Log4j2
@Accessors(fluent=true)
public final class IcarusPlugin extends AbstractPlugin {
  /**
   * Define if server is a subserver or standalone.
   * True if value is set in spigot.yml to 'true' otherwise 'false'.
   */
  @Getter
  @Setter
  private boolean proxy;

  @Override
  public void configure() {
    this
        .handle(PluginState.PRE_ENABLE, () -> {
          //Unregister unwanted commands.
          this.unregisterCommand("tps");
          this.unregisterCommand("reload");
          this.unregisterCommand("rl");
        })

        //Handle client.
        .handle(PluginState.POST_ENABLE, () -> {
          //Enable client
          Client.enable();


          Client.client()
              //Handle establish
              .handle(ClientState.ESTABLISHED, () -> {
                //Run sync -> bukkit
                this.sync(() -> {
                  Bukkit
                      .getOnlinePlayers()
                      .stream()
                      //Filter online players, this state is only for double security.
                      .filter(player -> player.hasPermission(Permission.OFFLINE))
                      //Broadcast message
                      .forEach(player -> {
                        player.sendMessage(Message.CLIENT_ONLINE_INFO);
                      });
                });

              })
              //Handle failed
              .handle(ClientState.FAILED, () -> {
                //Run sync -> bukkit
                this.sync(() -> {
                  Bukkit
                      .getOnlinePlayers()
                      .forEach(player -> {
                        //Check if player has permission -> inform about connection loss.
                        if (player.hasPermission(Permission.OFFLINE)) {
                          player.sendMessage(Message.CLIENT_OFFLINE_INFO);
                        } else {
                          //Kick
                          player.kick(Message.CLIENT_OFFLINE_KICK);
                        }
                      });
                });

              });
        })

        //Set icarus
        .handle(PluginState.POST_ENABLE, () -> {
          //Get configuration value of bungee-cord value in spigot.yml
          this.proxy(YamlConfiguration
              //Load config file.
              .loadConfiguration(new File("spigot.yml"))
              //Get if boolean is true.
              .getBoolean("settings.bungeecord"));

          log.info(this.proxy() ? "Proxy present." : "No proxy present.");
        });
  }


  /**
   * Unregister command from server.
   *
   * @param commandString command to unregister.
   */
  private void unregisterCommand(@NotNull final String commandString) {
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