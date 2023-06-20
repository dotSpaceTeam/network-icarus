package dev.dotspace.network.node.profile.db;

import dev.dotspace.network.library.profile.ProfileType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jetbrains.annotations.NotNull;

@Converter
public final class ProfileTypeConverter implements AttributeConverter<ProfileType, Integer> {
  /**
   * Convert {@link ProfileType} to {@link Integer} using {@link ProfileType#id()}.
   */
  @Override
  public @NotNull Integer convertToDatabaseColumn(@NotNull final ProfileType attribute) {
    return attribute.id();
  }

  /**
   * Convert {@link Integer} to {@link ProfileType} using {@link ProfileType#fromId(int)}.
   */
  @Override
  public @NotNull ProfileType convertToEntityAttribute(@NotNull final Integer dbData) {
    return ProfileType.fromId(dbData);
  }
}
