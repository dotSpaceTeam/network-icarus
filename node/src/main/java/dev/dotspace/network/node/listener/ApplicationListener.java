package dev.dotspace.network.node.listener;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public final class ApplicationListener {

  @EventListener
  public void handleEvent(@NotNull final ApplicationReadyEvent event) {

  }
}
