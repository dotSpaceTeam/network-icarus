package dev.dotspace.network.client.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

@Accessors(fluent = true)
public abstract class AbstractRequest {
  @Autowired
  @Getter(AccessLevel.PROTECTED)
  private IRestClient client;
}
