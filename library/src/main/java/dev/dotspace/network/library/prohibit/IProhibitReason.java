package dev.dotspace.network.library.prohibit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


//Swagger
@Schema(implementation=ImmutableProhibitReason.class)
public interface IProhibitReason {
  /**
   * Type for prohibit reason.
   *
   * @return defines the reason affiliation.
   */
  //Swagger
  @Schema(example="KICK", description="Type of prohibit.")
  @NotNull ProhibitType type();

  /**
   * How the reason should be named for commands.
   *
   * @return name of reason.
   */
  //Swagger
  @Schema(example="bye", description="Short name to find reason.")
  @NotNull String name();

  /**
   * How the reason should be titled.
   *
   * @return title of reason.
   */
  //Swagger
  @Schema(example="Bye!", description="Title/Prefix of reason for info message.")
  @NotNull String title();

  /**
   * Description of prohibit.
   *
   * @return description as name.
   */
  //Swagger
  @Schema(example="We don't need you on our server.", description="Description of prohibit.")
  @NotNull String description();
}
