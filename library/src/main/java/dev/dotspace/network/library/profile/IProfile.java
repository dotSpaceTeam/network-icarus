package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


//Swagger
@Schema(implementation=ImmutableProfile.class)
public interface IProfile extends IUniqueId {
  /**
   * Type of profile.
   *
   * @return type of profile.
   */
  //Swagger
  @Schema(example="JAVA", description="Type of profile stored under id.")
  @NotNull ProfileType profileType();
}
