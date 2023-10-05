package dev.dotspace.network.rabbitmq.publisher;

import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import org.jetbrains.annotations.Nullable;


/**
 * Publish message with client.
 */
public interface IRabbitPublisher {

  boolean publish(@Nullable final String key,
                  final byte @Nullable [] content) throws RabbitClientAbsentException, RabbitIOException;

  <TYPE> boolean publish(@Nullable final String key,
                         @Nullable final Class<TYPE> mapperClass,
                         @Nullable final TYPE type) throws RabbitIOException, RabbitClientAbsentException;

  boolean publish(@Nullable final String exchange,
                  @Nullable final String key,
                  final byte @Nullable [] content) throws RabbitClientAbsentException, RabbitIOException;

  <TYPE> boolean publish(@Nullable final String exchange,
                         @Nullable final String key,
                         @Nullable final Class<TYPE> mapperClass,
                         @Nullable final TYPE type) throws RabbitIOException, RabbitClientAbsentException;
}
