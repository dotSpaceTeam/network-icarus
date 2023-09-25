package dev.dotspace.network.library.game.message.context;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;
import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.common.function.ThrowableSupplier;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.parser.MessageParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


@Log4j2
@Accessors(fluent=true)
public abstract class AbstractMessageContext implements IMessageContext {
  /**
   * Global cache for all context.
   */
  //static
  private final static @NotNull MessageCache CACHE = new MessageCache();
  private final @NotNull List<ThrowableConsumer<String>> handleList;

  private final @NotNull Map<String, ThrowableSupplier<?>> placeholderMap;
  @Getter
  private final @NotNull ContextType contextType;
  private final @NotNull Locale locale;
  @Getter(AccessLevel.PROTECTED)
  private @NotNull String content;

  private boolean ignoreCache = false;


  public AbstractMessageContext(@Nullable final ContextType contextType,
                                @Nullable final String content,
                                @Nullable final Locale locale) {
    //Null check
    Objects.requireNonNull(contextType);
    Objects.requireNonNull(content);
    Objects.requireNonNull(locale);

    this.placeholderMap = new HashMap<>();
    this.handleList = new ArrayList<>();
    this.contextType = contextType;
    this.content = content;
    this.locale = locale;
  }

  @Override
  public @NotNull <REPLACE_TYPE> IMessageContext replace(@Nullable String replaceText,
                                                         @Nullable REPLACE_TYPE content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    //Add replace content.
    this.placeholderMap.put(replaceText, () -> content /*Create empty supplier.*/);
    return this;
  }

  @Override
  public @NotNull <REPLACE_TYPE> IMessageContext replace(@Nullable String replaceText,
                                                         @Nullable ThrowableSupplier<REPLACE_TYPE> content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    //Add replace content.
    this.placeholderMap.put(replaceText, content);
    return this;
  }

  /**
   * See {@link IMessageContext#ignoreCache()}
   */
  @Override
  public @NotNull IMessageContext ignoreCache() {
    this.ignoreCache = true;
    return this;
  }

  @Override
  public @NotNull String forceComplete() {
    return this.process();
  }

  @Override
  public @NotNull Response<String> complete() {
    return Library.responseService().response(this::process);
  }

  /**
   * See {@link IMessageContext#handle(ThrowableConsumer)}
   */
  @Override
  public @NotNull IMessageContext handle(@Nullable ThrowableConsumer<String> handleConsumer) {
    //Null check
    Objects.requireNonNull(handleConsumer);

    //Add handler
    this.handleList.add(handleConsumer);

    return this;
  }

  protected @NotNull String process() {
    //Create parser.
    final MessageParser messageParser = new MessageParser();

    try {
      //Send server request.
      this.content = this.serverRequest();
    } catch (final InterruptedException ignore) {
      //can be ignored.
    }

    //Handle placeholder
    messageParser.handle("PLACEHOLDER", context -> {
      //Return context if null.
      if (context == null) {
        return;
      }

      final String field = context.valueField();
      final String replace = Optional
          //Get content.
          .ofNullable(this.placeholderMap.get(field))
          .map(throwableSupplier -> {
            try {
              //Format placeholder
              return String.valueOf(throwableSupplier.get());
            } catch (final Throwable exception) {
              throw new RuntimeException(exception);
            }
          })
          .orElse("%"+field+"%");

      //Update message.
      this.content = context.replace(this.content, replace);
    });

    //Parse message
    messageParser.parse(this.content);

    //Loop trough context.
    for (final ThrowableConsumer<String> consumer : this.handleList) {
      try {
        //Consume content to handle.
        consumer.accept(this.content);
      } catch (final Throwable throwable) {
        //Error
        log.warn("Error while handling context.", throwable);
      }
    }

    return this.content;
  }

  private @NotNull String serverRequest() throws InterruptedException {
    //Return content if context is offline.
    if (this.contextType == ContextType.OFFLINE) {
      return this.content;
    }

    //Return content if client is not connected.
    if (Client.disconnected()) {
      log.warn("Can't prepare message={}, client is offline!", this.content);
      return this.content;
    }

    //Todo implement locale.
    if (this.contextType == ContextType.ONLINE) {
      return Client.client()
          .messageRequest()
          //Send message to format
          .formatString(this.content)
          .getOptional()
          .map(IMessage::message)
          .orElse("No message to format found!");
    }

    //Search for key in cache.
    if (!this.ignoreCache) {
      //Get stored message.
      @Nullable final String message = CACHE.get(this.content, this.locale);

      //Return message if present.
      if (message != null) {
        return message;
      }
    }

    return Client.client()
        .messageRequest()
        //Get message from key.
        .getMessage(this.locale, this.content)
        .getOptional()
        .map(iMessage -> {
          //Get requested message.
          final String message = iMessage.message();

          //Cache if not deactivated.
          if (!this.ignoreCache) {
            //Write to cache
            CACHE.cache(this.content, message, locale);
          }

          return message;
        })
        .orElse("Error while requesting key!");
  }

  //private methods

  /**
   * Cache for locale messages.
   */
  @Singleton
  @Log4j2
  private static final class MessageCache {
    /**
     * {@link Cache} instance to hold message.
     */
    private final @NotNull Cache<String, String> messageCache;

    public MessageCache() {
      //Cache duration
      final Duration duration = Duration.ofMinutes(5);

      //Configure cache
      this.messageCache = CacheBuilder.newBuilder()
          //Expire message after 5 minutes
          .expireAfterWrite(duration)
          .build();

      //Log
      log.info("Message cache initialize. Cache time={} Minutes.", duration.toMinutes());
    }

    /**
     * Cache message.
     */
    public void cache(@Nullable final String key,
                      @Nullable final String message,
                      @Nullable final Locale locale) {
      //Return if one parameter is null.
      if (key == null || message == null || locale == null) {
        return;
      }

      //Cache key to store message.
      final String combinedKey = this.key(key, locale);

      //Cache message.
      this.messageCache.put(combinedKey, message);

      //Log
      log.debug("Cached message={}.", combinedKey);
    }

    /**
     * Cache message.
     */
    public @Nullable String get(@Nullable final String key,
                                @Nullable final Locale locale) {
      //Return if one parameter is null.
      if (key == null || locale == null) {
        return null;
      }

      //Cache key to store message.
      final String combinedKey = this.key(key, locale);

      //Get cached message or null message.
      return this.messageCache.getIfPresent(combinedKey);
    }


    /**
     * Build key from key and locale.
     */
    private @NotNull String key(@NotNull final String key,
                                @NotNull final Locale locale) {
      return key+locale.toLanguageTag();
    }
  }
}