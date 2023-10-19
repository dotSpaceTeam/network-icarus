package dev.dotspace.network.library.velocity.self;

import com.velocitypowered.api.plugin.Plugin;
import dev.dotspace.network.library.velocity.plugin.AbstractPlugin;


@Plugin(id="icarus",
    name="Icarus",
    version="1.0",
    url="https://git.pluginstube.dev/pluginstube/development/icarus",
    description="Handle icarus.",
    authors={"Dani_R"})
public final class IcarusPlugin extends AbstractPlugin {
  @Override
  public void configure() {
    //Add plugin module.
    this.module(new SelfModule());
  }
}
