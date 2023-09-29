package dev.dotspace.network.node.system.db;

import dev.dotspace.network.library.system.ParticipantType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;

/**
 * Convert {@link ParticipantType} to id and id to {@link ParticipantType}.
 */

@Converter
public final class ParticipantTypeConverter implements AttributeConverter<ParticipantType, Integer> {
  /**
   * Convert {@link ParticipantType} to {@link Integer} using {@link ParticipantType#id()}.
   */
  @Override
  public @NotNull Integer convertToDatabaseColumn(@NotNull final ParticipantType attribute) {
    return attribute.id();
  }

  /**
   * Convert {@link Integer} to {@link ParticipantType} using {@link ParticipantType#fromId(int)}.
   */
  @Override
  public @NotNull ParticipantType convertToEntityAttribute(@NotNull final Integer dbData) {
    return ParticipantType.fromId(dbData);
  }
}
