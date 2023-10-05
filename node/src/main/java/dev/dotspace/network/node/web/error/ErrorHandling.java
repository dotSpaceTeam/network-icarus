package dev.dotspace.network.node.web.error;

import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
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
  @ExceptionHandler(ElementNotPresentException.class)
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
  private @NotNull ImmutableResponse elementNotPresentException(@NotNull final ElementNotPresentException exception) {
    return new ImmutableResponse("Element is not present.", exception.getMessage(), HttpStatus.NOT_FOUND.value());
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
              schema=@Schema(implementation=ImmutableResponse.class)
          )
      }
  )
  private @NotNull ImmutableResponse elementException(@NotNull final ElementException exception) {
    return new ImmutableResponse("Something went wrong.", exception.getMessage(), HttpStatus.CONFLICT.value());
  }
}
