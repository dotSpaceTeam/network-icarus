package dev.dotspace.network.rabbitmq.exception;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;


public final class RabbitIOException extends RabbitException {
  /**
   * Create exception.
   *
   * @param exception caused this error.
   */
  public RabbitIOException(@Nullable final IOException exception) {
    super(exception != null && exception.getMessage() != null ? exception.getMessage() : "No io error provided.");
  }
}
