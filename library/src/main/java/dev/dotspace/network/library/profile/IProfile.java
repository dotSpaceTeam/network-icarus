package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;


//Swagger
@Tag(name="Test")
@Schema(implementation=ImmutableProfile.class)
public interface IProfile {
  /**
   * UniqueId of player given by mojang and used for system.
   *
   * @return uniqueId as {@link String}.
   */
  //Swagger
  @Schema(description="UniqueId given by mojang for player.")
  @NotNull String uniqueId();

  /**
   * Type of profile.
   *
   * @return type of profile.
   */
  @Schema(description="Type of profile stored under id.")
  @NotNull ProfileType profileType();
}
