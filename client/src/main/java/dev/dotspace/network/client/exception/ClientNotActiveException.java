package dev.dotspace.network.client.exception;

import dev.dotspace.network.library.exception.LibraryRuntimeException;


public final class ClientNotActiveException extends LibraryRuntimeException {
  /**
   * Create exception.
   */
  public ClientNotActiveException() {
    super("Tried to use client, but client is not active.");
  }
}
