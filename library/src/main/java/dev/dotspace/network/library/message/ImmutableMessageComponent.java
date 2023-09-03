package dev.dotspace.network.library.message;

import dev.dotspace.network.library.message.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;


public record ImmutableMessageComponent(@NotNull String message,
                                        @NotNull PlaceholderCollection placeholderCollection
) implements IMessageComponent {

}
