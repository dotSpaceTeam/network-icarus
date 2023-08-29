package dev.dotspace.network.node.message.text;

import dev.dotspace.network.library.message.content.IContentPlaceholder;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public record ImmutableTextMessage(@NotNull String plainText,
                                   @Nullable Set<IContentPlaceholder<?>> placeholders) implements ITextMessage {
  /**
   * See {@link ITextMessage#replace(String, Object)}.
   */
  @Override
  public @NotNull <TYPE> ITextMessage replace(@Nullable String string,
                                              @Nullable TYPE replace) {
    this
      .findPlaceholder(string) /* Find key in placeholders set. */

      /* If placeholder present replace context. */
      .ifPresent(objectIPlaceholder -> objectIPlaceholder.replaceContext(replace));
      return this;
  }

  /**
   * Find placeholder in set.
   *
   * @param string placeholder code to find.
   * @param <TYPE> generic type of placeholder.
   */
  @SuppressWarnings("unchecked")
  private <TYPE> @NotNull Optional<@Nullable IContentPlaceholder<TYPE>> findPlaceholder(@Nullable final String string) {
    // Return empty if placeholders are null.
    if (this.placeholders == null) {
      return Optional.empty();
    }

    for (final IContentPlaceholder<?> placeholder : this.placeholders) { //Loop trough placeholders.
      if (!placeholder.code().equals(string)) { //Goto next placeholder if code does not match.
        continue;
      }
      try {
        return Optional.of((IContentPlaceholder<TYPE>) placeholder); //Return found placeholder.
      } catch (final ClassCastException exception) {
        log.error("Placeholder is not needed type! ({})", exception.getMessage());
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

    for (final IContentPlaceholder<?> placeholder : this.placeholders) { //Loop trough every placeholder.
      preparedMessage = preparedMessage.replace(placeholder.replaceKey(), placeholder
        .replaceContext()
        .map(Object::toString)
        .orElse(placeholder.code()));
    }
    return preparedMessage; //Return map message.
  }
}
