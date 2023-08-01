package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.ImmutableRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public abstract class AbstractPlugin extends JavaPlugin implements IPlugin {
  private final @NotNull IRuntime runtime;

  //Initialize plugin instance.
  {
    this.runtime = ImmutableRuntime.randomOfType(RuntimeType.CLIENT);
  }

  @Override
  public final void onLoad() {
    log.info("Instance running under id={}.", this.runtime.runtimeId());
    //Pass load
    this.load();
  }

  @Override
  public final void onEnable() {
    //Pass enable
    this.enable();
  }

  @Override
  public final void onDisable() {
    //Pass disable
    this.disable();
  }
}
