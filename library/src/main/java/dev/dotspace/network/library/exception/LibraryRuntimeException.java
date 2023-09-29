package dev.dotspace.network.library.exception;

/**
 * Base error from library.
 */
public class LibraryRuntimeException extends RuntimeException {
  /**
   * Create exception.
   *
   * @param message caused this error.
   */
  public LibraryRuntimeException(String message) {
    super(message);
  }
}
