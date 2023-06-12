package dev.dotspace.network.node.message.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.ImmutableMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

@Component("messageDatabase")
public final class MessageDatabase implements IMessageDatabase {
  /**
   * Logger
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(MessageDatabase.class);

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private MessageKeyRepository messageKeyRepository;

  /**
   * See {@link IMessageDatabase#insertMessage(Locale, String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<Boolean> insertMessage(@Nullable Locale locale,
                                                             @Nullable String key,
                                                             @Nullable String message) {
    return this.handleUpdate(locale, key, message, false);
  }

  /**
   * See {@link IMessageDatabase#updateMessage(Locale, String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<Boolean> updateMessage(@Nullable Locale locale,
                                                             @Nullable String key,
                                                             @Nullable String message) {
    return this.handleUpdate(locale, key, message, true);
  }

  /**
   * Update or insert message.
   *
   * @param locale      to insert message for.
   * @param key         of message.
   * @param message     text content self.
   * @param allowUpdate true, if message can be updated if already present.
   * @return response.
   */
  private CompletableResponse<Boolean> handleUpdate(@Nullable final Locale locale,
                                                    @Nullable final String key,
                                                    @Nullable final String message,
                                                    final boolean allowUpdate) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(locale);
      Objects.requireNonNull(key);
      Objects.requireNonNull(message);

      final String localeTag = locale.toLanguageTag();
      final MessageKeyEntity messageKey = this.insertIfAbsentAndGetLocale(key);

      @Nullable final MessageEntity messageEntity = this.messageRepository
        .findByKeyAndLocale(messageKey, localeTag)
        .orElse(null);

      if (messageEntity == null) {
        this.messageRepository.save(new MessageEntity(messageKey, localeTag, message));
        return true;
      }

      //No update
      if (!allowUpdate) {
        return false;
      }

      messageEntity.message(message);
      this.messageRepository.save(messageEntity);
      return true;
    });
  }


  /**
   * See {@link IMessageDatabase#message(Locale, String)}.
   */
  @Override
  public @NotNull CompletableResponse<IMessage> message(@Nullable Locale locale,
                                                        @Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(locale);
      Objects.requireNonNull(key);

      final MessageKeyEntity messageKey = this.messageKeyRepository
        .findByKey(key)
        .orElseThrow(() -> {
          LOGGER.error("No key='{}' present, can't find message.", key);
          return new NullPointerException();
        });

      final String localeTag = locale.toLanguageTag();

      return this.messageRepository
        /*
         * Find key and locale.
         */
        .findByKeyAndLocale(messageKey, localeTag)
        /*
         * Map to message.
         */
        .map(ImmutableMessage::of)
        .orElseThrow();
    });
  }

  /**
   * Insert key if absent.
   *
   * @param key to insert.
   */
  private @NotNull MessageKeyEntity insertIfAbsentAndGetLocale(@NotNull final String key) {


    return this.messageKeyRepository
      /*
       * Return this if present.
       */
      .findByKey(key)
      /*
       * Otherwise create.
       */
      .orElseGet(() -> this.messageKeyRepository.save(new MessageKeyEntity(key)));
  }
}
