package dev.dotspace.network.library.reflections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

public final class ReflectionHelper {

  public static @NotNull Field field(@Nullable final Class<?> fieldClass,
                                     @Nullable final String name) {
    //Null check
    Objects.requireNonNull(fieldClass);
    Objects.requireNonNull(name);

    try {
      final Field privateField = fieldClass.getDeclaredField(name);
      privateField.setAccessible(true);
      return privateField;
    } catch (final NoSuchFieldException exception) {
      throw new RuntimeException(exception);
    }
  }
}
