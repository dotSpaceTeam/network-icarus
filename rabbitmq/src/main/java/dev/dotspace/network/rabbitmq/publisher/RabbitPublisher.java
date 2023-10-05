package dev.dotspace.network.rabbitmq.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.AMQP.BasicProperties;
import dev.dotspace.network.rabbitmq.IRabbitClient;
import dev.dotspace.network.rabbitmq.RabbitField;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import dev.dotspace.network.rabbitmq.paritcipant.RabbitParticipant;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Log4j2
public final class RabbitPublisher extends RabbitParticipant implements IRabbitPublisher {

  private final @NotNull BasicProperties publishProperties;

  public RabbitPublisher(@Nullable final IRabbitClient rabbitClient,
                         @Nullable final Duration expirationDuration) {
    //Pass to parent.
    super(rabbitClient);

    //Create properties.
    final BasicProperties.Builder builder = new BasicProperties()
        .builder()
        //Content type.
        .contentType("application/json");

    //Apply expirationDuration if present.
    if (expirationDuration != null) {
      //Time to set for ttl in milliseconds.
      builder.expiration(Long.toString(expirationDuration.toMillis()));
    }

    //Build
    this.publishProperties = builder.build();
  }

  @Override
  public boolean publish(@Nullable String key,
                         byte @Nullable [] content) throws RabbitClientAbsentException, RabbitIOException {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(content);

    //Pass to method
    return this.publishMessage("" /*No exchange.*/, key, content, this.publishProperties);
  }

  @Override
  public <TYPE> boolean publish(@Nullable String key,
                                @Nullable Class<TYPE> mapperClass,
                                @Nullable TYPE type) throws RabbitIOException, RabbitClientAbsentException {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(mapperClass);
    Objects.requireNonNull(type);

    return this.publishObject("", key, mapperClass, type, this.publishProperties);
  }

  @Override
  public boolean publish(@Nullable String exchange,
                         @Nullable String key,
                         byte @Nullable [] content) throws RabbitClientAbsentException, RabbitIOException {
    //Null check
    Objects.requireNonNull(exchange);
    Objects.requireNonNull(key);
    Objects.requireNonNull(content);

    //Pass to method
    return this.publishMessage(exchange, key, content, this.publishProperties);
  }

  @Override
  public <TYPE> boolean publish(@Nullable String exchange,
                                @Nullable String key,
                                @Nullable Class<TYPE> mapperClass,
                                @Nullable TYPE type) throws RabbitIOException, RabbitClientAbsentException {
    //Null check
    Objects.requireNonNull(exchange);
    Objects.requireNonNull(key);
    Objects.requireNonNull(mapperClass);
    Objects.requireNonNull(type);

    return this.publishObject(exchange, key, mapperClass, type, this.publishProperties);
  }

  private boolean publishMessage(@NotNull final String exchange,
                                 @NotNull final String key,
                                 final byte @NotNull [] content,
                                 @NotNull final BasicProperties properties) throws RabbitClientAbsentException, RabbitIOException {
    //Get channel and use it -> closed in function.

    try {
      //Publish content.
      this.rabbitClient().channel().basicPublish(exchange, key, properties, content);

      //Log
      log.debug("Published to key={} and content-length={}.", key, content.length);
      return true;
    } catch (final IOException exception) {
      //Pass error.
      throw new RabbitIOException(exception);
    }
  }

  private <TYPE> boolean publishObject(@NotNull final String exchange,
                                       @NotNull final String key,
                                       @NotNull final Class<TYPE> mapperClass,
                                       @NotNull final TYPE type,
                                       @NotNull final BasicProperties properties) throws RabbitIOException, RabbitClientAbsentException {
    //Get content of file.
    final byte[] content;
    try {
      //Set content
      content = this.mapper().writeValueAsBytes(type);
    } catch (final JsonProcessingException exception) {
      throw new RabbitIOException(exception);
    }

    //Create new map with already existing headers.
    final Map<String, Object> headers =
        //Create copy if headers are present otherwise create empty map.
        properties.getHeaders() != null ? new HashMap<>(properties.getHeaders()) : new HashMap<>();

    //Add name of class to make deserialize easy.
    headers.put(RabbitField.MAPPER_CLASS, mapperClass.getName());

    return this.publishMessage(exchange, key, content, properties.builder().headers(headers).build() /*Add headers.*/);
  }
}
