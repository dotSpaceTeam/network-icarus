package dev.dotspace.network.node.web.error;

import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.node.exception.ElementException;
import io.swagger.v3.oas.annotations.Operation;
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
              schema=@Schema(implementation=ImmutableErrorResponse.class)
          )
      }
  )
  private ImmutableErrorResponse authenticationHandler(AuthenticationException exception) {
    return new ImmutableErrorResponse(
        "Not allowed! ("+exception.getMessage()+")",
        HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(ElementException.class)
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
  private @NotNull ImmutableErrorResponse elementException(@NotNull final ElementException exception) {
    return new ImmutableErrorResponse(
        "Something went wrong. ("+exception.getMessage()+")",
        HttpStatus.CONFLICT.value());
  }
}
