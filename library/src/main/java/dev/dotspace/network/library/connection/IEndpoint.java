package dev.dotspace.network.library.connection;

import jakarta.validation.constraints.NotNull;


/**
 * Endpoint info.
 */
public interface IEndpoint {
  /**
   * Get endpoint name.
   *
   * @return name of endpoint (Mostly used url path of rest-api).
   */
  @NotNull String endpoint();
}
