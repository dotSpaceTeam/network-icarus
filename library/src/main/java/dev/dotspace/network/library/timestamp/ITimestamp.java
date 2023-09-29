package dev.dotspace.network.library.timestamp;

import dev.dotspace.network.library.profile.ImmutableProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Date;


//Swagger
@Schema(implementation=ImmutableProfile.class)
public interface ITimestamp {
  /**
   * {@link Date} stored the timestamp.
   */
  //Swagger
  @Schema(example="Fri Sep 29 09:47:10 CEST 2023", description="Timestamp stored in java date format.")
  @NotNull Date timestamp();
}
