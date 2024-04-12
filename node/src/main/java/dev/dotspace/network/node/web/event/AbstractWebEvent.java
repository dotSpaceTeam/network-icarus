package dev.dotspace.network.node.web.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;


/**
 * Base of web events.
 */
public abstract class AbstractWebEvent extends ApplicationEvent {
  /**
   * See {@link ApplicationEvent#ApplicationEvent(Object)}.
   */
  public AbstractWebEvent(@NotNull final Object source) {
    super(source);
  }
}
