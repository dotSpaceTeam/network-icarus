package dev.dotspace.network.node.web;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.common.response.Response;
import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.node.exception.ElementException;
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
   * @return response. Code 200 or 404 ->
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
}
