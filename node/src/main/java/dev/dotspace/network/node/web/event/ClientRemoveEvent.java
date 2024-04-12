package dev.dotspace.network.node.web.event;

import org.jetbrains.annotations.NotNull;


/**
 * Events if client was marked as expired.
 */
public final class ClientRemoveEvent extends AbstractClientEvent {
  /**
   * See {@link AbstractWebEvent#AbstractWebEvent(Object)}.
   */
  public ClientRemoveEvent(@NotNull final Object source,
                           @NotNull final String clientId) {
    super(source, clientId);
  }
}
