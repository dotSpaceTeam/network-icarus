package dev.dotspace.network.node.message.text;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * Create Message instance.
 *
 * @param plainText    message itself.
 * @param placeholders list of placeholders to use in message.
 */
public record ImmutableTextMessage(@NotNull String plainText,
                                   @Nullable Set<IPlaceholder<?>> placeholders) implements ITextMessage {
  /**
   * Logger
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(ImmutableTextMessage.class);

  /**
   * See {@link ITextMessage#replace(String, Object)}.
   */
  @Override
  public @NotNull <TYPE> ITextMessage replace(@Nullable String string,
                                              @Nullable TYPE replace) {
    /* Create new PlaceholderContext for a given object. */
    return this.replace(string, () -> replace);
  }

  /**
   * See {@link ITextMessage#replace(String, PlaceholderContext)}.
   */
  @SuppressWarnings("unchecked")
  @Override
  public @NotNull <TYPE> ITextMessage replace(@Nullable String string,
                                              @Nullable PlaceholderContext<@Nullable TYPE> replaceContext) {
    this
      .findPlaceholder(string) /* Find key in placeholders set. */

      /* If placeholder present replace context. */
      .ifPresent(objectIPlaceholder -> objectIPlaceholder.replaceContext((PlaceholderContext<Object>) replaceContext));
    return this;
  }

  /**
   * Find placeholder in set.
   *
   * @param string placeholder code to find.
   * @param <TYPE> generic type of placeholder.
   */
  @SuppressWarnings("unchecked")
  private <TYPE> @NotNull Optional<@Nullable IPlaceholder<TYPE>> findPlaceholder(@Nullable final String string) {
    // Return empty if placeholders are null.
    if (this.placeholders == null) {
      return Optional.empty();
    }

    for (final IPlaceholder<?> placeholder : this.placeholders) { //Loop trough placeholders.
      if (!placeholder.code().equals(string)) { //Goto next placeholder if code does not match.
        continue;
      }
      try {
        return Optional.of((IPlaceholder<TYPE>) placeholder); //Return found placeholder.
      } catch (final ClassCastException exception) {
        LOGGER.error("Placeholder is not needed type! ({})", exception.getMessage());
      }
    }
    return Optional.empty();
  }

  /**
   * Format message.
   * See {@link ITextMessage#formatted()}.
   */
  @Override
  public @NotNull String formatted() {
    String preparedMessage = this.plainText; //Get text.

    //Return prepared message if no placeholders where set.
    if (this.placeholders == null) {
      return preparedMessage;
    }

    for (final IPlaceholder<?> placeholder : this.placeholders) { //Loop trough every placeholder.
      preparedMessage = preparedMessage.replace(placeholder.replaceKey(), placeholder
        .replaceContext()
        .map(Supplier::get /* Get the context of IPlaceholderContext. */)
        .map(Object::toString)
        .orElse(placeholder.code()));
    }
    return preparedMessage; //Return map message.
  }
}
