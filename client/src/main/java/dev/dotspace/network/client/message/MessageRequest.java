package dev.dotspace.network.client.message;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.ImmutableMessage;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.content.ImmutablePersistentMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;


public final class MessageRequest extends AbstractRequest implements IMessageRequest {
  /**
   * See {@link IMessageRequest#format(String)}
   */
  @Override
  public @NotNull Response<IMessage> format(@Nullable String message) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(message);

          return this.client().post("/v1/message/",
              ImmutableMessage.class,
              /*Create instance to send*/ImmutableMessage.of(message));
        });
  }

  /**
   * See {@link IMessageRequest#message(Locale, String)}
   */
  @Override
  public @NotNull Response<IMessage> message(@Nullable Locale locale,
                                             @Nullable String key) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(locale);
          Objects.requireNonNull(key);

          return this.client().get("/v1/message/key/"+key+"?lang="+locale.toLanguageTag(),
              ImmutableMessage.class);
        });
  }

  /**
   * See {@link IMessageRequest#update(Locale, String, String)}
   */
  @Override
  public @NotNull Response<IPersistentMessage> update(@Nullable Locale locale,
                                                      @Nullable String key,
                                                      @Nullable String message) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(locale);
          Objects.requireNonNull(key);
          Objects.requireNonNull(message);

          return this.client().post("/v1/message/key",
              ImmutablePersistentMessage.class,
              new ImmutablePersistentMessage(key, locale, message));
        });
  }

  /**
   * See {@link IMessageRequest#create(Locale, String, String)}
   */
  @Override
  public @NotNull Response<IPersistentMessage> create(@Nullable Locale locale,
                                                      @Nullable String key,
                                                      @Nullable String message) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(locale);
          Objects.requireNonNull(key);
          Objects.requireNonNull(message);

          return this.client().put("/v1/message/key",
              ImmutablePersistentMessage.class,
              new ImmutablePersistentMessage(key, locale, message));
        });
  }
}