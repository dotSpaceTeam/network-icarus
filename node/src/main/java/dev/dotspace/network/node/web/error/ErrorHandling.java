package dev.dotspace.network.node.web.error;

import dev.dotspace.network.node.database.exception.EntityException;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import dev.dotspace.network.node.database.exception.IllegalEntityException;
import dev.dotspace.network.node.web.controller.AbstractRestController;
import dev.dotspace.network.node.web.controller.IResponse;
import dev.dotspace.network.node.web.controller.ImmutableErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public final class ErrorHandling extends AbstractRestController {
  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  //Swagger
  @ApiResponse(
      responseCode="401",
      description="Unauthorized request.",
      content={
          @Content(
              mediaType=MediaType.APPLICATION_JSON_VALUE,
              schema=@Schema(implementation=ImmutableErrorResponse.class)
          )
      }
  )
  private IResponse authenticationHandler(@NotNull final AuthenticationException exception) {
    return ImmutableErrorResponse.create(HttpStatus.UNAUTHORIZED, "Access denied! ("+exception.getMessage()+")");
  }

  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(EntityNotPresentException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  //Swagger
  @ApiResponse(
      responseCode="404",
      description="No matching entity found.",
      content={
          @Content(
              mediaType=MediaType.APPLICATION_JSON_VALUE,
              schema=@Schema(implementation=ImmutableErrorResponse.class)
          )
      }
  )
  private @NotNull IResponse elementNotPresentException(@NotNull final EntityNotPresentException exception) {
    return ImmutableErrorResponse.create(HttpStatus.NOT_FOUND, "Entity is not present. ("+exception.getMessage()+")");
  }

  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(IllegalEntityException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  //Swagger
  @ApiResponse(
      responseCode="409",
      description="Error with entity you deployed.",
      content={
          @Content(
              mediaType=MediaType.APPLICATION_JSON_VALUE,
              schema=@Schema(implementation=ImmutableErrorResponse.class)
          )
      }
  )
  private @NotNull IResponse elementIllegalException(@NotNull final IllegalEntityException exception) {
    return ImmutableErrorResponse.create(HttpStatus.CONFLICT, "Error with given entity. ("+exception.getMessage()+")");
  }

  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(EntityException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  //Swagger
  @ApiResponse(
      responseCode="409",
      description="Conflict while processing entity.",
      content={
          @Content(
              mediaType=MediaType.APPLICATION_JSON_VALUE,
              schema=@Schema(implementation=ImmutableErrorResponse.class)
          )
      }
  )
  private @NotNull IResponse elementException(@NotNull final EntityException exception) {
    return ImmutableErrorResponse.create(HttpStatus.CONFLICT, "Something went wrong. ("+exception.getMessage()+")");
  }
}
