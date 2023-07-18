package dev.dotspace.network.node.message.text;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstract implementation for {@link IPlaceholder}.
 */
@Accessors(fluent = true)
@Log4j2
public final class Placeholder<TYPE> implements IPlaceholder<TYPE> {
  /**
   * See {@link IPlaceholder#replaceKey()}.
   */
  @Getter
  private final @NotNull String replaceKey;
  /**
   * See {@link IPlaceholder#code()}.
   */
  @Getter
  private final @NotNull String code;

  private @Nullable TYPE replaceContext;

  /**
   * Construct instance of {@link Placeholder}.
   *
   * @param replaceKey key to replace in text.
   * @param code       replace index in replaceKey. ({{ PLACEHOLDER:OS }} -> OS is the code.)
   * @throws NullPointerException if one key is null.
   */
  public Placeholder(@Nullable final String replaceKey,
                     @Nullable final String code) {
    this.replaceKey = Objects.requireNonNull(replaceKey);
    this.code = Objects.requireNonNull(code).toUpperCase();
  }

  /**
   * See {@link IPlaceholder#replaceContext()}.
   */
  @Override
  public @NotNull Optional<TYPE> replaceContext() {
    return Optional.ofNullable(this.replaceContext);
  }

  /**
   * See {@link IPlaceholder#replaceContext(TYPE)}.
   */
  @Override
  public @NotNull IPlaceholder<TYPE> replaceContext(@Nullable final TYPE replaceContext) {
    this.replaceContext = replaceContext;
    return this;
  }

  /**
   * Pass hash code of replace key.
   */
  @Override
  public int hashCode() {
    return replaceKey.hashCode();
  }

  /**
   * Compare implementation.
   */
  @Override
  public boolean equals(@Nullable final Object object) {
    if (this == object) {
      return true; //Equal reference.
    }

    return object instanceof IPlaceholder<?> placeholder &&
      placeholder.replaceKey().equals(this.replaceKey); //If both replace patterns are equal -> same object.
  }
}
