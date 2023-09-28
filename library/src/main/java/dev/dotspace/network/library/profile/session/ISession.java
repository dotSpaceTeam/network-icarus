package dev.dotspace.network.library.profile.session;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Date;


/**
 * Session info.
 */
//Swagger
@Schema(implementation=ImmutableSession.class)
public interface ISession extends IPlaytime {
  /**
   * Session id.
   */
  //Swagger
  @Schema(description="Incrementing id of session. (Incremented by database.)")
  @NotNull Long sessionId();

  /**
   * Time the session has started.
   *
   * @return time as {@link Date}.
   */
  //Swagger
  @Schema(description="Timestamp whenever session began.")
  @NotNull Date startDate();

  /**
   * Time the session has ended.
   *
   * @return time as {@link Date}.
   */
  //Swagger
  @Schema(description="Timestamp whenever session has been closed. (Or is still open because something went wrong.)")
  @NotNull Date endDate();
}
