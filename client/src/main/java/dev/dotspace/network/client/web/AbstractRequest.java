package dev.dotspace.network.client.web;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class AbstractRequest {
  /**
   * Client for request.
   */
  @Getter(AccessLevel.PROTECTED)
  @Inject
  private IRestClient client;
}
