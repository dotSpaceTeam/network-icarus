package dev.dotspace.network.node.web;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.common.response.Response;
import dev.dotspace.common.response.ResponseService;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;


@ControllerAdvice
@Accessors(fluent=true)
public abstract class AbstractRestController {
  /**
   * Async service for rest service.
   */
  @Autowired
  @Getter
  private ResponseService responseService;

  /**
   * Create response.
   *
   * @param type         to wrap as json.
   * @param errorMessage if type is null.
   * @param <TYPE>       generic type of object.
   * @return response. Code 200 or 404 -> handled by {@link AbstractRestController#nullPointerHandler(NullPointerException)}.
   */
  protected <TYPE> ResponseEntity<TYPE> validateOkResponse(@Nullable final TYPE type,
                                                           @Nullable final String errorMessage) {
    /*
     * Run without message.
     */
    if (errorMessage == null) {
      return ResponseEntity.ok(Objects.requireNonNull(type));
    }
    /*
     * With message.
     */
    return ResponseEntity.ok(Objects.requireNonNull(type, errorMessage));
  }

  /**
   * Create response.
   *
   * @param completableResponse response to wait for complete.
   * @param errorMessage        if type is null.
   * @param <TYPE>              generic type of object.
   * @throws InterruptedException if {@link CompletableResponse} was interrupted.
   */
  protected <TYPE> ResponseEntity<TYPE> validateOkResponse(@Nullable final Response<TYPE> completableResponse,
                                                           @Nullable final String errorMessage) throws InterruptedException {
    //Null check
    Objects.requireNonNull(completableResponse, "Response is null");

    return this.validateOkResponse(completableResponse.get(), errorMessage);
  }

  /**
   * Handle system interrupt errors.
   */
  @ResponseBody
  @ExceptionHandler(InterruptedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  private String interruptHandler(InterruptedException exception) {
    return "System interrupted (%s)".formatted(exception.getMessage());
  }

  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  private String nullPointerHandler(NullPointerException exception) {
    return "Requested resource is null, or parameter was null. (%s)".formatted(exception.getMessage());
  }

  /**
   * Handle null pointer errors.
   */
  @ResponseBody
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  private String authenticationHandler(AuthenticationException exception) {
    return "Not allowed! (%s)".formatted(exception.getMessage());
  }
}
