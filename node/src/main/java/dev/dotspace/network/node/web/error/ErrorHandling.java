package dev.dotspace.network.node.web.error;

import dev.dotspace.network.node.database.exception.EntityException;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import dev.dotspace.network.node.database.exception.IllegalEntityException;
import dev.dotspace.network.node.web.ImmutableResponse;
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
public final class ErrorHandling {
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
              schema=@Schema(implementation=ImmutableResponse.class)
          )
      }
  )
  private ImmutableResponse authenticationHandler(@NotNull final AuthenticationException exception) {
    return new ImmutableResponse("Access denied!", exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
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
              schema=@Schema(implementation=ImmutableResponse.class)
          )
      }
  )
  private @NotNull ImmutableResponse elementNotPresentException(@NotNull final EntityNotPresentException exception) {
    return new ImmutableResponse("Entity is not present.", exception.getMessage(), HttpStatus.NOT_FOUND.value());
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
              schema=@Schema(implementation=ImmutableResponse.class)
          )
      }
  )
  private @NotNull ImmutableResponse elementIllegalException(@NotNull final IllegalEntityException exception) {
    return new ImmutableResponse("Error with given entity.", exception.getMessage(), HttpStatus.CONFLICT.value());
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
              schema=@Schema(implementation=ImmutableResponse.class)
          )
      }
  )
  private @NotNull ImmutableResponse elementException(@NotNull final EntityException exception) {
    return new ImmutableResponse("Something went wrong.", exception.getMessage(), HttpStatus.CONFLICT.value());
  }
}
