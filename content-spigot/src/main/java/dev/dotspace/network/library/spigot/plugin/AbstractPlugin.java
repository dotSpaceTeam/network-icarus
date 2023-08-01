package dev.dotspace.network.library.spigot.plugin;


import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public abstract class AbstractPlugin extends JavaPlugin implements IPlugin {
  @Override
  public final void onLoad() {
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
