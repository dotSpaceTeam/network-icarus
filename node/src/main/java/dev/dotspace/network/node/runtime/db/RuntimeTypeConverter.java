package dev.dotspace.network.node.runtime.db;

import dev.dotspace.network.library.runtime.RuntimeType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;

/**
 * Convert {@link RuntimeType} to id and id to {@link RuntimeType}.
 */

@Converter
public final class RuntimeTypeConverter implements AttributeConverter<RuntimeType, Integer> {
  /**
   * Convert {@link RuntimeType} to {@link Integer} using {@link RuntimeType#id()}.
   */
  @Override
  public @NotNull Integer convertToDatabaseColumn(@NotNull final RuntimeType attribute) {
    return attribute.id();
  }

  /**
   * Convert {@link Integer} to {@link RuntimeType} using {@link RuntimeType#fromId(int)}.
   */
  @Override
  public @NotNull RuntimeType convertToEntityAttribute(@NotNull final Integer dbData) {
    return RuntimeType.fromId(dbData);
  }
}
