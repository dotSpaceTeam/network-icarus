package dev.dotspace.network.node.message_old.text;

import dev.dotspace.network.library.message.old.content.IContentPlaceholder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstract implementation for {@link IContentPlaceholder}.
 */
@Accessors(fluent = true)
@Log4j2
public final class Placeholder<TYPE> implements IContentPlaceholder<TYPE> {
  /**
   * See {@link IContentPlaceholder#replaceKey()}.
   */
  @Getter
  private final @NotNull String replaceKey;
  /**
   * See {@link IContentPlaceholder#code()}.
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
   * See {@link IContentPlaceholder#replaceContext()}.
   */
  @Override
  public @NotNull Optional<TYPE> replaceContext() {
    return Optional.ofNullable(this.replaceContext);
  }

  /**
   * See {@link IContentPlaceholder#replaceContext(TYPE)}.
   */
  @Override
  public @NotNull IContentPlaceholder<TYPE> replaceContext(@Nullable final TYPE replaceContext) {
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

    return object instanceof IContentPlaceholder<?> placeholder &&
      placeholder.replaceKey().equals(this.replaceKey); //If both replace patterns are equal -> same object.
  }
}
