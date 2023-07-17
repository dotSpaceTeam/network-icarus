package dev.dotspace.network.node.economy.db;

import dev.dotspace.network.library.economy.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;

@Converter
public final class TransactionTypeConverter implements AttributeConverter<TransactionType, Integer> {
  /**
   * Convert {@link TransactionType} to {@link Integer}.
   */
  @Override
  public @NotNull Integer convertToDatabaseColumn(@NotNull final TransactionType attribute) {
    return attribute.id();
  }

  /**
   * Convert {@link Integer} to {@link TransactionType}.
   */
  @Override
  public @NotNull TransactionType convertToEntityAttribute(@NotNull final Integer dbData) {
    return TransactionType.fromId(dbData);
  }
}
