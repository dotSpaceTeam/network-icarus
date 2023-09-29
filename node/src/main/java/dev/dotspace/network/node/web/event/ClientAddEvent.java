package dev.dotspace.network.node.web.event;

import org.jetbrains.annotations.NotNull;


/**
 * Fires if client is marked as new.
 */
public final class ClientAddEvent extends AbstractClientEvent {
  /**
   * See {@link AbstractWebEvent#AbstractWebEvent(Object)}.
   */
  public ClientAddEvent(@NotNull Object source, @NotNull String clientId) {
    super(source, clientId);
  }
}
