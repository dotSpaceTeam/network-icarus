package dev.dotspace.network.rabbitmq.publisher;

import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import org.jetbrains.annotations.Nullable;


/**
 * Publish message with client.
 */
public interface IRabbitPublisher {
  /**
   * Publish content for a key.
   *
   * @param key     to publish message for -> if empty just create an empty string.
   * @param content object bytes.
   * @return true, if published successfully.
   * @throws RabbitClientAbsentException if connection is not present or and error with the connection.
   * @throws RabbitIOException           while parsing the content.
   */
  boolean publish(@Nullable final String key,
                  final byte @Nullable [] content) throws RabbitClientAbsentException, RabbitIOException;

  /**
   * Publish an object.
   *
   * @param key         to object message.
   * @param mapperClass to put in header for deserializer.
   * @param type        object to send.
   * @param <TYPE>      generic type of object.
   * @return true, if published successfully.
   * @throws RabbitClientAbsentException if connection is not present or and error with the connection.
   * @throws RabbitIOException           while parsing the content.
   */
  <TYPE> boolean publish(@Nullable final String key,
                         @Nullable final Class<TYPE> mapperClass,
                         @Nullable final TYPE type) throws RabbitIOException, RabbitClientAbsentException;

  /**
   * Publish content for a key and exchange.
   *
   * @param exchange to publish message in.
   * @param key      to publish message for -> if empty just create an empty string.
   * @param content  object bytes.
   * @return true, if published successfully.
   * @throws RabbitClientAbsentException if connection is not present or and error with the connection.
   * @throws RabbitIOException           while parsing the content.
   */
  boolean publish(@Nullable final String exchange,
                  @Nullable final String key,
                  final byte @Nullable [] content) throws RabbitClientAbsentException, RabbitIOException;

  /**
   * Publish an object for a key and exchange.
   *
   * @param exchange    to publish object in.
   * @param key         to object message.
   * @param mapperClass to put in header for deserializer.
   * @param type        object to send.
   * @param <TYPE>      generic type of object.
   * @return true, if published successfully.
   * @throws RabbitClientAbsentException if connection is not present or and error with the connection.
   * @throws RabbitIOException           while parsing the content.
   */
  <TYPE> boolean publish(@Nullable final String exchange,
                         @Nullable final String key,
                         @Nullable final Class<? extends TYPE> mapperClass,
                         @Nullable final TYPE type) throws RabbitIOException, RabbitClientAbsentException;
}
