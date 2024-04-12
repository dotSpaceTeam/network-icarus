package dev.dotspace.network.node.connection.db;

import dev.dotspace.network.library.connection.InternetProtocolVersion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;

@Converter
public final class InternetProtocolVersionConverter implements AttributeConverter<InternetProtocolVersion, Integer> {
  /**
   * Convert {@link InternetProtocolVersion} to {@link Integer} using {@link InternetProtocolVersion#id()}.
   */
  @Override
  public @NotNull Integer convertToDatabaseColumn(@NotNull final InternetProtocolVersion attribute) {
    return attribute.id();
  }

  /**
   * Convert {@link Integer} to {@link InternetProtocolVersion} using {@link InternetProtocolVersion#fromId(int)}.
   */
  @Override
  public @NotNull InternetProtocolVersion convertToEntityAttribute(@NotNull final Integer dbData) {
    return InternetProtocolVersion.fromId(dbData);
  }
}
