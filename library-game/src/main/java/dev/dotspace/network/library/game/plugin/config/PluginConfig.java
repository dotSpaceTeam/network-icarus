package dev.dotspace.network.library.game.plugin.config;

import dev.dotspace.network.library.config.IConfigFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


/**
 * Plugin configuration.
 */
@NoArgsConstructor
@Accessors(fluent=true)
public final class PluginConfig implements IConfigFile {
  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean clientAutoConnect;

  //static

  /**
   * Default config
   */
  public static @NotNull PluginConfig defaultConfig() {
    // Create new plain config.
    return new PluginConfig()
        //Default no connect.
        .clientAutoConnect(false);
  }
}
