package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


public interface IUniqueId {
  /**
   * UniqueId of player given by mojang and used for system.
   *
   * @return uniqueId as {@link String}.
   */
  //Swagger
  @Schema(
      example="aa701217-4b37-4958-8062-037f26740a5d",
      description="UniqueId given by mojang for player.")
  @NotNull String uniqueId();
}
