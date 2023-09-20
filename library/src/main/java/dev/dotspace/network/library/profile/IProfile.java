package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


//Swagger
@Schema(implementation=ImmutableProfile.class)
public interface IProfile {
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

  /**
   * Type of profile.
   *
   * @return type of profile.
   */
  @Schema(example="JAVA", description="Type of profile stored under id.")
  @NotNull ProfileType profileType();
}
