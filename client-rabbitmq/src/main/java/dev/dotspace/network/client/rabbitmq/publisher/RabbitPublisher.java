package dev.dotspace.network.client.rabbitmq.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.AMQP.BasicProperties;
import dev.dotspace.network.client.rabbitmq.paritcipant.RabbitParticipant;
import dev.dotspace.network.library.data.KeyValuePair;
import dev.dotspace.network.client.rabbitmq.IRabbitClient;
import dev.dotspace.network.client.rabbitmq.RabbitField;
import dev.dotspace.network.client.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.client.rabbitmq.exception.RabbitIOException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Log4j2
public final class RabbitPublisher extends RabbitParticipant implements IRabbitPublisher {
  /**
   * Properties with pre-builder header.
   */
  private final @NotNull BasicProperties publishProperties;

  public RabbitPublisher(@Nullable final IRabbitClient rabbitClient,
                         @Nullable final Duration expirationDuration) {
    //Pass to parent.
    super(rabbitClient);

    //Create properties.
    final BasicProperties.Builder builder = new BasicProperties()
        .builder()
        //Content type.
        .contentType(RabbitField.APPLICATION_JSON);

    //Apply expirationDuration if present.
    if (expirationDuration != null) {
      //Time to set for ttl in milliseconds.
      builder.expiration(Long.toString(expirationDuration.toMillis()));
    }

    //Build
    this.publishProperties = builder.build();
  }

  /**
   * See {@link IRabbitPublisher#publish(String, byte[], List)} .
   */
  @Override
  public boolean publish(@Nullable String key,
                         byte @Nullable [] content,
                         @Nullable List<KeyValuePair> pairs) throws RabbitClientAbsentException, RabbitIOException {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(content);

    //Pass to method
    return this.publishMessage("" /*No exchange.*/, key, content, this.publishProperties, pairs);
  }

  /**
   * See {@link IRabbitPublisher#publish(String, Class, Object, List)}
   */
  @Override
  public <TYPE> boolean publish(@Nullable String key,
                                @Nullable Class<TYPE> mapperClass,
                                @Nullable TYPE type,
                                @Nullable List<KeyValuePair> pairs) throws RabbitIOException, RabbitClientAbsentException {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(mapperClass);
    Objects.requireNonNull(type);

    return this.publishObject("", key, mapperClass, type, this.publishProperties, pairs);
  }

  /**
   * See {@link IRabbitPublisher#publish(String, String, byte[], List)}
   */
  @Override
  public boolean publish(@Nullable String exchange,
                         @Nullable String key,
                         byte @Nullable [] content,
                         @Nullable List<KeyValuePair> pairs) throws RabbitClientAbsentException, RabbitIOException {
    //Null check
    Objects.requireNonNull(exchange);
    Objects.requireNonNull(key);
    Objects.requireNonNull(content);

    //Pass to method
    return this.publishMessage(exchange, key, content, this.publishProperties, pairs);
  }

  /**
   * See {@link IRabbitPublisher#publish(String, Class, Object, List)}
   */
  @Override
  public <TYPE> boolean publish(@Nullable String exchange,
                                @Nullable String key,
                                @Nullable Class<? extends TYPE> mapperClass,
                                @Nullable TYPE type,
                                @Nullable List<KeyValuePair> pairs) throws RabbitIOException, RabbitClientAbsentException {
    //Null check
    Objects.requireNonNull(exchange);
    Objects.requireNonNull(key);
    Objects.requireNonNull(mapperClass);
    Objects.requireNonNull(type);

    return this.publishObject(exchange, key, mapperClass, type, this.publishProperties, pairs);
  }

  /**
   * Implementation for publish a message.
   */
  private boolean publishMessage(@NotNull final String exchange,
                                 @NotNull final String key,
                                 final byte @NotNull [] content,
                                 @NotNull final BasicProperties properties,
                                 @Nullable final List<KeyValuePair> pairs) throws RabbitClientAbsentException,
      RabbitIOException {
    //Get channel and use it -> closed in function.

    try {
      //Publish content.
      this.rabbitClient().channel().basicPublish(exchange, key, this.buildProperties(properties, pairs), content);

      //Log
      log.debug("Published to key={} and content-length={}.", key, content.length);
      return true;
    } catch (final IOException exception) {
      //Pass error.
      throw new RabbitIOException(exception);
    }
  }

  /**
   * Implementation for object publishing.
   */
  private <TYPE> boolean publishObject(@NotNull final String exchange,
                                       @NotNull final String key,
                                       @NotNull final Class<? extends TYPE> mapperClass,
                                       @NotNull final TYPE type,
                                       @NotNull final BasicProperties properties,
                                       @Nullable final List<KeyValuePair> pairs) throws RabbitIOException, RabbitClientAbsentException {
    //Get content of file.
    final byte[] content;
    try {
      //Set content
      content = this.mapper().writeValueAsBytes(type);
    } catch (final JsonProcessingException exception) {
      throw new RabbitIOException(exception);
    }

    //Create new list if absent.
    final List<KeyValuePair> updatePairs = pairs == null ? new ArrayList<>() : pairs;

    //Add class
    updatePairs.add(new KeyValuePair(RabbitField.MAPPER_CLASS, mapperClass.getName()));

    //Pass to message.
    return this.publishMessage(exchange, key, content, properties, updatePairs);
  }

  private @NotNull BasicProperties buildProperties(@NotNull final BasicProperties properties,
                                                   @Nullable final List<KeyValuePair> pairs) {
    //If pair null -> return
    if (pairs == null) {
      return properties;
    }

    //Create new map with already existing headers.
    final Map<String, Object> headers =
        //Create copy if headers are present otherwise create empty map.
        properties.getHeaders() != null ? new HashMap<>(properties.getHeaders()) : new HashMap<>();

    //Add key and value of pair
    for (final KeyValuePair pair : pairs) {
      //Add pair to properties
      headers.put(pair.key(), pair.value());
    }

    //Build updated properties.
    return properties.builder().headers(headers).build();
  }
}
