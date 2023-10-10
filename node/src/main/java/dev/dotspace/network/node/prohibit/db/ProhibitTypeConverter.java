package dev.dotspace.network.node.prohibit.db;

import dev.dotspace.network.library.prohibit.ProhibitType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;

@Converter
public final class ProhibitTypeConverter implements AttributeConverter<ProhibitType, Integer> {
  /**
   * Convert {@link ProhibitType} to {@link Integer} using {@link ProhibitType#id()}.
   */
  @Override
  public @NotNull Integer convertToDatabaseColumn(@NotNull final ProhibitType attribute) {
    return attribute.id();
  }

  /**
   * Convert {@link Integer} to {@link ProhibitType} using {@link ProhibitType#fromId(int)}.
   */
  @Override
  public @NotNull ProhibitType convertToEntityAttribute(@NotNull final Integer dbData) {
    return ProhibitType.fromId(dbData);
  }
}
