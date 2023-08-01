package dev.dotspace.network.client.web;

import dev.dotspace.network.library.provider.Provider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class AbstractRequest implements Provider {
  /**
   * Client for request.
   */
  @Getter(AccessLevel.PROTECTED)
  private final IRestClient client;

  protected AbstractRequest(IRestClient client) {
    this.client = client;
  }
}
