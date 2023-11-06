package dev.dotspace.network.client.rabbitmq.exception;

import dev.dotspace.network.library.exception.LibraryException;


/**
 * Base error for further errors.
 */
public class RabbitException extends LibraryException {
  /**
   * Create exception.
   *
   * @param message caused this error.
   */
  public RabbitException(String message) {
    super(message);
  }
}
