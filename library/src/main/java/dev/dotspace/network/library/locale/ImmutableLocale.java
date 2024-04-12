package dev.dotspace.network.library.locale;

import dev.dotspace.network.library.message.IMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable implementation of {@link IMessage}.
 */
@Schema(
    name="Locale",
    description="Locale tag."
)
public record ImmutableLocale(@NotNull String locale) implements ILocale {
}
