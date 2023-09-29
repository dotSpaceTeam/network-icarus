package dev.dotspace.network.node.web.event;

import dev.dotspace.network.library.connection.IRestRequest;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


/**
 * Fires if client is marked as new.
 */
@Getter
@Accessors(fluent=true)
public final class RequestResponseEvent extends AbstractWebEvent {
  /**
   * Get request for event.
   */
  private final @NotNull IRestRequest request;

  /**
   * See {@link AbstractWebEvent#AbstractWebEvent(Object)}.
   */
  public RequestResponseEvent(@NotNull final Object source,
                              @NotNull final IRestRequest request) {
    super(source);
    this.request = request;
  }
}
