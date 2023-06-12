package dev.dotspace.network.node.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public abstract class AbstractRestController {
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
}
