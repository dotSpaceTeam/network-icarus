package dev.dotspace.network.node.message.web;

import dev.dotspace.network.library.message.v2.IMessage;
import dev.dotspace.network.library.message.v2.ImmutableMessage;
import dev.dotspace.network.library.message.v2.content.IPersistentMessage;
import dev.dotspace.network.library.message.v2.content.ImmutablePersistentMessage;
import dev.dotspace.network.library.message.v2.parser.MessageParser;
import dev.dotspace.network.library.message.v2.parser.TextElement;
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

import java.util.Locale;
import java.util.Optional;


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

  @Autowired
  private MessageParser messageParser;

  @PostConstruct
  public void init() {
    this.messageParser
        .handle(TextElement.PLACEHOLDER, (textElement, locale, text, value, option) -> {

    });
  }

  /**
   * Get a formatted message.
   */
  @PostMapping("/")
  @ResponseBody
  public ResponseEntity<IMessage> postMessage(@RequestBody @NotNull final ImmutableMessage message,
                                              @RequestParam(required=false) final String lang) throws InterruptedException {
    //Get message or default
    final Locale locale = this.localeFromTag(lang);

    System.out.println(this.messageParser.parse(message.message(), locale));

    return this.validateOkResponse(this.responseService().response(() -> message), "NullMessage");
  }

  /**
   * Get an profile from unique id.
   */
  @PutMapping("/key")
  @ResponseBody
  public ResponseEntity<IPersistentMessage> putKey(@RequestBody @NotNull final ImmutablePersistentMessage message) throws InterruptedException {
    return this.validateOkResponse(this.messageDatabase
            .insertMessage(message.locale(), message.key(), message.message()),
        "Can't set (put) message '%s' to key '%s'.".formatted(message.message(), message.key()));
  }

  /**
   * Get an profile from unique id.
   */
  @PostMapping("/key")
  @ResponseBody
  public ResponseEntity<IPersistentMessage> postKey(@RequestBody @NotNull final ImmutablePersistentMessage message) throws InterruptedException {
    return this.validateOkResponse(this.messageDatabase
            .updateMessage(message.locale(), message.key(), message.message()),
        "Can't set (post) message '%s' to key '%s'.".formatted(message.message(), message.key()));
  }

  /**
   * Get an profile from unique id.
   */
  @GetMapping("/key/{key}")
  @ResponseBody
  public ResponseEntity<IPersistentMessage> getKey(@PathVariable @NotNull final String key,
                                                   @RequestParam(required=false) final String lang) throws InterruptedException {
    final Locale locale = this.localeFromTag(lang);

    //Return value
    return this.validateOkResponse(
        this.messageDatabase.message(locale, key),
        "No message found for %s with locale %s.".formatted(key, locale.toLanguageTag()));
  }

  /**
   * Convert {@link String} with lang tag to {@link Locale}.
   *
   * @param lang to convert.
   * @return present locale otherwise using {@link Locale#getDefault()}.
   */
  private @NotNull Locale localeFromTag(@Nullable final String lang) {
    return Optional
        .ofNullable(lang)
        .map(s -> s.replaceAll("_", "-"))
        //Convert string to locale tag.
        .map(Locale::forLanguageTag)
        .orElseGet(() -> {
          log.warn("Can't parse locale tag '{}', using default instead.", lang);
          return Locale.getDefault();
        });
  }

}
