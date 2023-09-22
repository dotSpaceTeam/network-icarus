package dev.dotspace.network.library.game.plugin.config;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(fluent=true)
public final class PluginConfig implements IConfigFile {
  @JsonProperty("clientAutoConnect")
  private boolean clientAutoConnect;
  @JsonProperty("clientApiEndpoint")
  private String clientApiEndpoint;

  //static

  /**
   * Default config
   */
  public static @NotNull PluginConfig defaultConfig() {
    // Create new plain config.
    return new PluginConfig()
        //Default no connect.
        .clientAutoConnect(false)
        //Default client endpoint, should work in test environment.
        .clientApiEndpoint("http://localhost:8443");
  }
}
