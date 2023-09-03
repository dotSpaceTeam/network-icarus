package dev.dotspace.network.node.message.db;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.content.IPersistentMessageManipulator;
import dev.dotspace.network.library.message.content.ImmutablePersistentMessage;
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
public final class PersistentMessageDatabase extends AbstractDatabase implements IPersistentMessageManipulator {
  /**
   * Registry for messages.
   */
  @Autowired
  private PersistentMessageRepository messageRepository;
  /**
   * Registry for message key.
   */
  @Autowired
  private PersistentMessageKeyRepository messageKeyRepository;

  /**
   * See {@link IPersistentMessageManipulator#insertMessage(Locale, String, String)}.
   */
  @Override
  public @NotNull Response<IPersistentMessage> insertMessage(@Nullable Locale locale,
                                                             @Nullable String key,
                                                             @Nullable String message) {
    return this.handleUpdate(locale, key, message, false);
  }

  /**
   * See {@link IPersistentMessageManipulator#updateMessage(Locale, String, String)}.
   */
  @Override
  public @NotNull Response<IPersistentMessage> updateMessage(@Nullable Locale locale,
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
  private Response<IPersistentMessage> handleUpdate(@Nullable final Locale locale,
                                                    @Nullable final String key,
                                                    @Nullable final String message,
                                                    final boolean allowUpdate) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(locale);
      Objects.requireNonNull(key);
      Objects.requireNonNull(message);

      final String localeTag = locale.toLanguageTag();
      final PersistentMessageKeyEntity messageKey = this.key(key);

      @Nullable final PersistentMessageEntity messageEntity = this.messageRepository
          .findByKeyAndLocale(messageKey, localeTag)
          .orElse(null);

      //If entity is null create new.
      if (messageEntity == null) {
        return ImmutablePersistentMessage
            .of(this.messageRepository.save(new PersistentMessageEntity(messageKey, localeTag, message)));
      }

      //No update
      if (!allowUpdate) {
        return null;
      }

      //Update message.
      messageEntity.message(message);

      //Store message.
      this.messageRepository.save(messageEntity);

      //Return message.
      return ImmutablePersistentMessage.of(messageEntity);
    });
  }


  /**
   * See {@link IPersistentMessageManipulator#message(Locale, String)}.
   */
  @Override
  public @NotNull Response<IPersistentMessage> message(@Nullable Locale locale,
                                                       @Nullable String key) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(locale);
      Objects.requireNonNull(key);

      final PersistentMessageKeyEntity messageKey = this.messageKeyRepository
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
          .map(ImmutablePersistentMessage::of)
          .orElseThrow();
    });
  }

  /**
   * Insert key if absent.
   *
   * @param key to insert.
   */
  private @NotNull PersistentMessageKeyEntity key(@NotNull final String key) {
    return this.messageKeyRepository
        /*
         * Return this if present.
         */
        .findByKey(key)
        /*
         * Otherwise create.
         */
        .orElseGet(() -> this.messageKeyRepository.save(new PersistentMessageKeyEntity(key)));
  }
}
