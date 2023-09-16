package dev.dotspace.network.library.velocity.self;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.message.IMessageComponent;
import dev.dotspace.network.library.velocity.plugin.AbstractPlugin;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;


@Plugin(id="icarus",
    name="Icarus",
    version="1.0",
    url="https://git.pluginstube.dev/pluginstube/development/icarus",
    description="Handle icarus.",
    authors={"Dani_R"})
public final class IcarusPlugin extends AbstractPlugin {

  @Inject
  public IcarusPlugin(@NotNull ProxyServer server,
                      @NotNull Logger logger) {
    super(server, logger);
  }

  @Override
  public void configure() {
    this

        .handle(PluginState.POST_DISABLE, () -> {
          System.out.println("Disabled.");
        });
  }

  @Override
  public @NotNull IMessageComponent<Component> persistentMessage(@Nullable String uniqueId, @Nullable String key) {
    return null;
  }
}
