package dev.dotspace.network.library.message.v2;

import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;


public record ImmutableMessageComponent(@NotNull String message,
                                        @NotNull PlaceholderCollection placeholderCollection
) implements IMessageComponent {

}
