package dev.dotspace.network.node.listener;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

@Component
public final class NodeListener {

  @EventListener
  public void handleEvent(@NotNull final RequestHandledEvent event) {
    System.out.println("-- RequestHandledEvent --");
    System.out.println(event.wasFailure());
    System.out.println(event);
  }
}
