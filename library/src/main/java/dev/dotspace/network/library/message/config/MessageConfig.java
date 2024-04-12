package dev.dotspace.network.library.message.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dotspace.network.library.config.IConfigFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Locale;
import java.util.Map;


/**
 * Config Data to store message for system.
 */
@NoArgsConstructor
@Accessors(fluent=true)
public final class MessageConfig implements IConfigFile {
  /**
   * In config key messageMap.
   * Example:
   * <code>
   * {
   * "de-DE":
   * {
   * "testPrefix": "Testnachricht",
   * "testPrefix2": "Testnachricht2"
   * }
   * }
   * </code>
   */
  @JsonProperty("messageMap")
  @Getter
  private Map<Locale, Map<String, String>> messageMap;
}