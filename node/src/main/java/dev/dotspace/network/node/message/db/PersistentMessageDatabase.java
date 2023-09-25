package dev.dotspace.network.node.message.db;

import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.content.ImmutablePersistentMessage;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementAlreadyPresentException;
import dev.dotspace.network.node.exception.ElementException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;


@Component("messageDatabase")
@Log4j2
public final class PersistentMessageDatabase extends AbstractDatabase {
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

  public @NotNull IPersistentMessage insertMessage(@Nullable Locale locale,
                                                   @Nullable String key,
                                                   @Nullable String message) throws ElementException {
    return this.handleUpdate(locale, key, message, false);
  }

  public @NotNull IPersistentMessage updateMessage(@Nullable Locale locale,
                                                   @Nullable String key,
                                                   @Nullable String message) throws ElementException {
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
  private @NotNull IPersistentMessage handleUpdate(@Nullable final Locale locale,
                                                   @Nullable final String key,
                                                   @Nullable final String message,
                                                   final boolean allowUpdate) throws ElementException {
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
      //Throw  error
      throw new ElementAlreadyPresentException(messageEntity, "Message key="+key+" already present.");
    }

    //Update message.
    messageEntity.message(message);

    //Store message.
    this.messageRepository.save(messageEntity);

    //Return message.
    return ImmutablePersistentMessage.of(messageEntity);
  }

  public @NotNull IPersistentMessage message(@Nullable Locale locale,
                                             @Nullable String key) throws ElementException {
    //Null check
    Objects.requireNonNull(locale);
    Objects.requireNonNull(key);

    final PersistentMessageKeyEntity messageKey = this.messageKeyRepository
        .findByKey(key)
        .orElseThrow(() ->
            new ElementException(null,
                "No key="+key+" for locale="+locale.toLanguageTag()+" present, can't find message"));

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
