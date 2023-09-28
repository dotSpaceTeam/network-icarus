package dev.dotspace.network.node.web;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 * Class for response content.
 */
@Schema(
    name="Error",
    description="Error object with message and code."
)
public record ImmutableResponse(@Schema(example="Ok", description="Message for response.") @NotNull String message,
                                @Schema(example="1695862448", description="Unix timestamp of response.") long timestamp,
                                @Schema(example="200", description="Status code of response.") int httpCode
) {
  public ImmutableResponse(@NotNull final String message,
                           final int httpCode) {
    this(message, System.currentTimeMillis(), httpCode);
  }
}
