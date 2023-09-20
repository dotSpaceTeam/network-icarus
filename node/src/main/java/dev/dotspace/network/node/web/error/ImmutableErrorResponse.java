package dev.dotspace.network.node.web.error;

import org.jetbrains.annotations.NotNull;


/**
 * Class for response content.
 */
public record ImmutableErrorResponse(@NotNull String message,
                                     long timestamp,
                                     int httpCode
) {
  public ImmutableErrorResponse(@NotNull final String message,
                                final int httpCode) {
    this(message, System.currentTimeMillis(),httpCode );
  }
}
