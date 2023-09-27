package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 * Profile id with value.
 */

//Swagger
@Schema(implementation=ImmutableProfileRecord.class)
public interface IProfileRecord extends IUniqueId {
  /**
   * Value of profile.
   *
   * @return value as {@link String}.
   */
  //Swagger
  @Schema(
      example="Name",
      description="Content of record value.")
  @NotNull String value();
}
