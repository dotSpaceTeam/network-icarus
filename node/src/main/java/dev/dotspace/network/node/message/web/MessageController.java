package dev.dotspace.network.node.message.web;

import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.ImmutableMessage;
import dev.dotspace.network.library.message.ImmutableTextMessage;
import dev.dotspace.network.node.message.db.MessageDatabase;
import dev.dotspace.network.node.message.text.ITextMessage;
import dev.dotspace.network.node.message.text.parser.TextParser;
import dev.dotspace.network.node.web.AbstractRestController;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/v1/message")
@Log4j2
public final class MessageController extends AbstractRestController {

  @Autowired
  private MessageDatabase messageDatabase;

  @Autowired
  private TextParser parser;

  @GetMapping("/locale")
  @ResponseBody
  public ResponseEntity<List<String>> getLocales() throws InterruptedException {

    //Return value
    return null;
  }

  /**
   * Get a formatted message.
   */
  @PostMapping("/")
  @ResponseBody
  public ResponseEntity<ITextMessage> getFormatted(@RequestBody @NotNull final ImmutableTextMessage message,
                                                   @RequestParam(required = false) final String lang) throws InterruptedException {
    final Locale locale = this.localeFromTag(lang);

    return this.validateOkResponse(this.parser.parse(message.message(), locale), "Error while creating.");
  }

  /**
   * Get an profile from unique id.
   */
  @PutMapping("/key")
  @ResponseBody
  public ResponseEntity<IMessage> putKey(@RequestBody @NotNull final ImmutableMessage message) throws InterruptedException {
    return this.validateOkResponse(this.messageDatabase
        .updateMessage(message.locale(), message.key(), message.message())
        .map(aBoolean -> aBoolean ? message : null),
      "Can't set message '%s' to key '%s'.".formatted(message.message(), message.key()));
  }


  /**
   * Get an profile from unique id.
   */
  @GetMapping("/key/{key}")
  @ResponseBody
  public ResponseEntity<IMessage> getKey(@PathVariable @NotNull final String key,
                                         @RequestParam(required = false) final String lang) throws InterruptedException {
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
