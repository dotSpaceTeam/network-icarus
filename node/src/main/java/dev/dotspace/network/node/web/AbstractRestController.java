package dev.dotspace.network.node.web;

import dev.dotspace.common.response.ResponseService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;


@Getter
@Accessors(fluent=true)
public abstract class AbstractRestController {
  /**
   * Async service for rest service.
   */
  @Autowired
  private ResponseService responseService;

  @PostConstruct
  public void init() {
    System.out.printf("Test");
  }
}
