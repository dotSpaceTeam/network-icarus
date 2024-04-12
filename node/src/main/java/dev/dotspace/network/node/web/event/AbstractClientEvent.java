package dev.dotspace.network.node.web.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@Getter
@Accessors(fluent=true)
public abstract class AbstractClientEvent extends AbstractWebEvent {
  /**
   * Client of event.
   */
  private final @NotNull String clientId;

  /**
   * See {@link AbstractWebEvent#AbstractWebEvent(Object)}.
   */
  public AbstractClientEvent(@NotNull final Object source,
                             @NotNull final String clientId) {
    super(source);

    this.clientId = clientId;
  }
}
