package dev.dotspace.network.library.spigot.plugin;


public interface IPlugin {
  /**
   * Run on load
   */
  void load();

  /**
   * Run on enable
   */
  void enable();

  /**
   * Run on disable
   */
  void disable();
}
