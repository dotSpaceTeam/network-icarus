package dev.dotspace.network.rabbitmq.subscriber;

import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRabbitSubscriber {

  @NotNull IRabbitSubscriber subscribe(@Nullable final String key,
                                       @Nullable final ThrowableConsumer<byte[]> consumer) throws RabbitIOException;

  @NotNull <TYPE> IRabbitSubscriber subscribe(@Nullable final String key,
                                              @Nullable final Class<TYPE> typeClass,
                                              @Nullable final ThrowableConsumer<TYPE> consumer) throws RabbitIOException;

}
