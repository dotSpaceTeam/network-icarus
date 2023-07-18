package dev.dotspace.network.library.message;

import org.jetbrains.annotations.NotNull;

public record ImmutableTextMessage(@NotNull String message) implements ITextMessage {
}
