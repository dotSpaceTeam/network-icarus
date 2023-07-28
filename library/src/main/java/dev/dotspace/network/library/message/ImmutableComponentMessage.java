package dev.dotspace.network.library.message;

import org.jetbrains.annotations.NotNull;

public record ImmutableComponentMessage(@NotNull String message) implements IMessageComponent {
}
