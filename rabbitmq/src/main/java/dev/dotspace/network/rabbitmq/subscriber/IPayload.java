package dev.dotspace.network.rabbitmq.subscriber;

import org.jetbrains.annotations.NotNull;

import java.util.Map;


public interface IPayload<PAYLOAD> {
  /**
   * Hold the payload of subscribe.
   *
   * @return instance of payload.
   */
  @NotNull PAYLOAD payload();

  /**
   * Get header map for payload.
   *
   * @return header.
   */
  @NotNull Map<String, Object> headers();
}
