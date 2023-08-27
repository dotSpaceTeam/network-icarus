package dev.dotspace.network.client.web;

import com.google.inject.Inject;
import dev.dotspace.common.response.ResponseService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;


@Accessors(fluent=true)
public abstract class AbstractRequest {
  /**
   * Client for request.
   */
  @Getter(AccessLevel.PROTECTED)
  @Inject
  private IRestClient client;

  /**
   * Response service to handle responses.
   */
  @Getter(AccessLevel.PROTECTED)
  @Inject
  private ResponseService responseService;
}
