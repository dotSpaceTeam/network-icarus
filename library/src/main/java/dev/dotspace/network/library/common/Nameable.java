package dev.dotspace.network.library.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


public interface Nameable {
  /**
   * Name of object.
   *
   * @return name ob object.
   */
  @Schema(example="The_One_And_Only",
      description="Name of data object.")
  @NotNull String name();
}
