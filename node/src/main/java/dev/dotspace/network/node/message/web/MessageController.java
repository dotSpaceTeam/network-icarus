package dev.dotspace.network.node.message.web;

import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.ImmutableMessage;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.content.ImmutablePersistentMessage;
import dev.dotspace.network.library.message.parser.MessageParser;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.message.db.PersistentMessageDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Rest controller for messages.
 */
@RestController
@RequestMapping("/v1/message")
@Log4j2
public final class MessageController extends AbstractRestController {
  /**
   * Database to handle.
   */
  @Autowired
  private PersistentMessageDatabase messageDatabase;

  /**
   * Get a formatted message.
   */
  @PostMapping("/")
  @ResponseBody
  public ResponseEntity<IMessage> postMessage(@RequestBody @NotNull final ImmutableMessage message,
                                              @RequestParam(required=false) final String lang) throws ElementException {
    //Get message or default
    final Locale locale = this.localeFromTag(lang);
    //Response to client.
    return ResponseEntity.ok(ImmutableMessage.of(this.message(locale, message.message())));
  }

  /**
   * Get an profile from unique id.
   */
  @PutMapping("/key")
  @ResponseBody
  public ResponseEntity<IPersistentMessage> putKey(@RequestBody @NotNull final ImmutablePersistentMessage message) throws ElementException {
    return ResponseEntity.ok(this.messageDatabase
        .insertMessage(message.locale(), message.key(), message.message()));
  }

  /**
   * Get an profile from unique id.
   */
  @PostMapping("/key")
  @ResponseBody
  public ResponseEntity<IPersistentMessage> postKey(@RequestBody @NotNull final ImmutablePersistentMessage message) throws ElementException {
    return ResponseEntity.ok(this.messageDatabase
        .updateMessage(message.locale(), message.key(), message.message()));
  }

  /**
   * Get an profile from unique id.
   */
  @GetMapping("/key/{key}")
  @ResponseBody
  public ResponseEntity<IPersistentMessage> getKey(@PathVariable @NotNull final String key,
                                                   @RequestParam(required=false) final String lang) throws ElementException {
    //Get message or default
    final Locale locale = this.localeFromTag(lang);
    //Get stored message.
    final IPersistentMessage persistentMessage = this.messageDatabase.message(locale, key);

    //Return value
    return ResponseEntity.ok(
        new ImmutablePersistentMessage(key, locale, this.message(locale, persistentMessage.message())));
  }

  /**
   * Convert {@link String} with lang tag to {@link Locale}.
   *
   * @param lang to convert.
   * @return present locale otherwise using {@link Locale#getDefault()}.
   */
  private @NotNull Locale localeFromTag(@Nullable final String lang) throws ElementNotPresentException {
    return Optional
        .ofNullable(lang)
        .map(s -> s.replaceAll("_", "-"))
        //Convert string to locale tag.
        .map(Locale::forLanguageTag)
        .orElseGet(Locale::getDefault);
  }

  private @NotNull String message(@NotNull final Locale locale,
                                  @NotNull final String message) {
    //Create new parser.
    final MessageParser messageParser = new MessageParser();
    //Reference to change in further
    final AtomicReference<String> reference = new AtomicReference<>(message);
    //List of already replaced messages -> to avoid loops.
    final List<String> presentKeys = new ArrayList<>();

    //Search for keys.
    messageParser
        .handle("KEY", matcherContext -> {
          //Return if context is empty.
          if (matcherContext == null) {
            return;
          }

          //Get key of tag.
          final String key = matcherContext.valueField();

          //Return if already replaced -> avoid death loops.
          if (presentKeys.contains(key)) {
            return;
          }

          //Add to present keys, if key will be double, function will be ignored. -> See above.
          presentKeys.add(key);

          //Get message of key.
          final IPersistentMessage persistentMessage = this.messageDatabase
              .message(locale, key);

          //Get message else replace.
          final String replaceText = persistentMessage.message();

          //Update reference of object.
          reference.set(matcherContext.replace(reference.get(), replaceText));
          //Reparse replaced message. to find further keys.
          messageParser.parse(reference.get());
        });

    //Run first loop.
    messageParser.parse(reference.get());

    //Response to client.
    return reference.get();
  }
}
