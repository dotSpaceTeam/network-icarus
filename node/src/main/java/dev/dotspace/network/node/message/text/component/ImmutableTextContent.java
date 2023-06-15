package dev.dotspace.network.node.message.text.component;

import org.jetbrains.annotations.NotNull;

/**
 * @param message see {@link ITextContent#message()}.
 */
public record ImmutableTextContent(@NotNull String message) implements ITextContent {
}
