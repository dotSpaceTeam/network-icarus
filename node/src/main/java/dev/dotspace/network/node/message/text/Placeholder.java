package dev.dotspace.network.node.message.text;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstract implementation for {@link IPlaceholder}.
 */
@Accessors(fluent = true)
public final class Placeholder<TYPE> implements IPlaceholder<TYPE> {
  /**
   * Logger.
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(Placeholder.class);
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

  private @Nullable PlaceholderContext<TYPE> replaceContext;

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
  public @NotNull Optional<PlaceholderContext<TYPE>> replaceContext() {
    return Optional.ofNullable(this.replaceContext);
  }

  /**
   * See {@link IPlaceholder#replaceContext(PlaceholderContext)}.
   */
  @Override
  public @NotNull IPlaceholder<TYPE> replaceContext(@Nullable final PlaceholderContext<TYPE> replaceContext) {
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
