package dev.dotspace.network.node.web;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

/**
 * Logger
 */
@Log4j2
@Getter
@Accessors(fluent=true)
public abstract class AbstractRestController {
  /**
   * Initialize controller.
   */
  @PostConstruct
  public void init() {
    log.info("Initialized rest-controller name={}.", this.getClass().getSimpleName());
  }
}
