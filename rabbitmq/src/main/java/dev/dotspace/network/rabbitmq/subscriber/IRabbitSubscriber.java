package dev.dotspace.network.rabbitmq.subscriber;

import dev.dotspace.common.function.ThrowableConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRabbitSubscriber {

  /**
   * Subscribe and read content as bytes.
   * Will be triggered by the main subscriber.
   *
   * @param consumer to consume bytes.
   * @return class instance.
   */
  @NotNull IRabbitSubscriber subscribe(@Nullable final ThrowableConsumer<byte[]> consumer);

  /**
   * Subscribe and read hole object.
   * Will be triggered by the main subscriber.
   *
   * @param typeClass class to read content as, only will be passed to consumer if header of message and this
   *                  parameter matches.
   * @param consumer  to consume present object.
   * @param <TYPE>    generic type of object to build and consume.
   * @return class instance.
   */
  @NotNull <TYPE> IRabbitSubscriber subscribe(@Nullable final Class<TYPE> typeClass,
                                              @Nullable final ThrowableConsumer<TYPE> consumer);

}
