package dev.dotspace.network.rabbitmq;

import com.rabbitmq.client.Channel;
import org.jetbrains.annotations.Nullable;


/**
 * Tools for rabbit
 */
public final class RabbitUtils {

  /**
   * Check if channel is active.
   *
   * @param channel to check for activity.
   * @return true, if present and active.
   */
  public static boolean active(@Nullable final Channel channel) {
    return channel != null && channel.isOpen();
  }
}
