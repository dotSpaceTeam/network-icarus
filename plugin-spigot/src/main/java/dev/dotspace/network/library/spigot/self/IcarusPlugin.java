package dev.dotspace.network.library.spigot.self;

import dev.dotspace.network.client.RestClient;
import dev.dotspace.network.client.web.ClientState;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.game.permission.Permission;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.spigot.inventory.InventoryProvider;
import dev.dotspace.network.library.spigot.itemstack.IItemProvider;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import dev.dotspace.network.library.spigot.self.message.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
          //Return if client is not enabled.
          if (!RestClient.present()) {
            return;
          }

          //Handle client behavior.
          RestClient.client()
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
        })

        .handle(PluginState.POST_ENABLE, () -> {

          this.getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class,
              new Listener() {
              },
              EventPriority.NORMAL,
              (listener, event) -> {
                final AsyncPlayerChatEvent chatEvent = (AsyncPlayerChatEvent) event;

                if (chatEvent.getMessage().equalsIgnoreCase("test")) {
                  this.provider(InventoryProvider.class)
                      .get()
                      .inventory(Bukkit.createInventory(null, 9))
                      .handle(InventoryClickEvent.class, inventoryClickEvent -> {
                        System.out.println("Clicked");
                      })
                      .handle(InventoryCloseEvent.class, inventoryCloseEvent -> {
                        System.out.println("Closed");
                      })
                      .open(((AsyncPlayerChatEvent) event).getPlayer());
                }

                if (chatEvent.getMessage().equalsIgnoreCase("item")) {
                  this.provider(IItemProvider.class)
                      .get()
                      .material(Material.DIRT)
                      .name(MessageContext.key("test", Locale.GERMANY))
                      .handle(itemStack -> {
                        ((AsyncPlayerChatEvent) event).getPlayer().getInventory().setItem(0, itemStack);
                      })
                      .complete();
                }

              }, this);

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