package dev.dotspace.network.node.message.db;

import dev.dotspace.network.library.data.DataManipulation;
import dev.dotspace.network.library.data.ImmutableListObject;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.content.ImmutablePersistentMessage;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.database.exception.EntityAlreadyPresentException;
import dev.dotspace.network.node.database.exception.EntityException;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;


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
                                                   @Nullable String message) throws EntityException {
    return this.handleUpdate(locale, key, message, false);
  }

  public @NotNull IPersistentMessage updateMessage(@Nullable Locale locale,
                                                   @Nullable String key,
                                                   @Nullable String message) throws EntityException {
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
                                                   final boolean allowUpdate) throws EntityException {
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
      //Created message
      final IPersistentMessage createdMessage = ImmutablePersistentMessage
          .of(this.messageRepository.save(new PersistentMessageEntity(messageKey, localeTag, message)));

      //Fire event -> Created message.
      this.publishEvent(createdMessage, ImmutablePersistentMessage.class, DataManipulation.CREATE);

      //Return
      return createdMessage;
    }

    //No update
    if (!allowUpdate) {
      //Throw  error
      throw new EntityAlreadyPresentException(messageEntity, "Message key="+key+" already present.");
    }

    //Update message.
    messageEntity.message(message);

    //Store message.
    this.messageRepository.save(messageEntity);

    //Create immutable.
    final IPersistentMessage updatedMessage = ImmutablePersistentMessage.of(messageEntity);

    //Fire event -> Updated message.
    this.publishEvent(updatedMessage, ImmutablePersistentMessage.class, DataManipulation.UPDATE);

    //Return
    return updatedMessage;
  }

  public @NotNull IPersistentMessage message(@Nullable Locale locale,
                                             @Nullable String key) throws EntityNotPresentException {
    return this.message(locale, key, false);
  }

  public @NotNull IPersistentMessage message(@Nullable final Locale locale,
                                             @Nullable final String key,
                                             final boolean findAny) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(locale);
    Objects.requireNonNull(key);

    final PersistentMessageKeyEntity messageKey = this.messageKeyRepository
        //Get message of key.
        .findByKey(key)
        .orElseThrow(() ->
            new EntityNotPresentException("No key="+key+" for locale="+locale.toLanguageTag()+" present, can't find message"));

    //Get locale to of locale.
    final String localeTag = locale.toLanguageTag();

    return this.messageRepository
        // Find key and locale.
        .findByKeyAndLocale(messageKey, localeTag)
        .or(() -> {
          //If findAny is false -> just ignore this tree. No message will be returned.
          if (!findAny) {
            return Optional.empty();
          }
          //Just search for key.
          return this.messageRepository.findByKey(messageKey)
              //Stream list
              .stream()
              //Find first.
              .findAny();
        })
        //Map to immutable.
        .map(ImmutablePersistentMessage::of)
        .orElseThrow(() -> new EntityNotPresentException("Key is not present in locale="+localeTag+"."));
  }

  public @NotNull ImmutableListObject<String> getLocaleList() {
    return new ImmutableListObject<>(this.messageRepository.getLocales());
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
