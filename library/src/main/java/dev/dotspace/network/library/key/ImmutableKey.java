package dev.dotspace.network.library.key;

import org.jetbrains.annotations.NotNull;

public record ImmutableKey(@NotNull String key) implements IKey {
}
