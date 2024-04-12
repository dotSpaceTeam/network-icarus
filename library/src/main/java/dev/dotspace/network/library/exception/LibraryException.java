package dev.dotspace.network.library.exception;

/**
 * Base error from library.
 */
public class LibraryException extends Exception {
  /**
   * Create exception.
   *
   * @param message caused this error.
   */
  public LibraryException(String message) {
    super(message);
  }
}
