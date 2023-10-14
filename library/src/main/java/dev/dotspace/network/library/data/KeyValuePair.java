package dev.dotspace.network.library.data;

import org.jetbrains.annotations.NotNull;


public record KeyValuePair(@NotNull String key,
                           @NotNull Object value) {
}
