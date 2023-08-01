package dev.dotspace.network.library.spigot.self;

import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.IcarusRuntime;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import dev.dotspace.network.library.spigot.scoreboard.SidebarProvider;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class IcarusPlugin extends AbstractPlugin {
  @Override
  public void load() {
    log.info("Loading ikarus spigot.");

    IcarusRuntime
      .providers()
      //Initialize providers.
      .provider(new SidebarProvider());

    log.info("Loaded ikarus spigot.");
  }

  @Override
  public void enable() {
    //Enable client.
    Client.enable();
  }

  @Override
  public void disable() {

  }
}
