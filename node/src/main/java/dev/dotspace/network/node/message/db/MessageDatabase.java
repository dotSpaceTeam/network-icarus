package dev.dotspace.network.node.message.db;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.IMessageManipulator;
import dev.dotspace.network.library.message.ImmutableMessage;
import dev.dotspace.network.node.database.AbstractDatabase;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;


@Component("messageDatabase")
@Log4j2
public final class MessageDatabase extends AbstractDatabase implements IMessageManipulator {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private MessageKeyRepository messageKeyRepository;

  /**
   * See {@link IMessageManipulator#insertMessage(Locale, String, String)}.
   */
  @Override
  public @NotNull Response<IMessage> insertMessage(@Nullable Locale locale,
                                                   @Nullable String key,
                                                   @Nullable String message) {
    return this.handleUpdate(locale, key, message, false);
  }

  /**
   * See {@link IMessageManipulator#updateMessage(Locale, String, String)}.
   */
  @Override
  public @NotNull Response<IMessage> updateMessage(@Nullable Locale locale,
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
  private Response<IMessage> handleUpdate(@Nullable final Locale locale,
                                          @Nullable final String key,
                                          @Nullable final String message,
                                          final boolean allowUpdate) {
    return this.responseService().response(() -> {
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
        return this.messageRepository.save(new MessageEntity(messageKey, localeTag, message));
      }

      //No update
      if (!allowUpdate) {
        return null;
      }

      messageEntity.message(message);
      this.messageRepository.save(messageEntity);
      return messageEntity;
    });
  }


  /**
   * See {@link IMessageManipulator#message(Locale, String)}.
   */
  @Override
  public @NotNull Response<IMessage> message(@Nullable Locale locale,
                                             @Nullable String key) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(locale);
      Objects.requireNonNull(key);

      final MessageKeyEntity messageKey = this.messageKeyRepository
          .findByKey(key)
          .orElseThrow(this.failOptional("No key='%s' present, can't find message.".formatted(key)));

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
