package dev.dotspace.network.node.message_old.web;

import dev.dotspace.network.library.message.old.ImmutableStringContent;
import dev.dotspace.network.node.message_old.text.ITextMessage;
import dev.dotspace.network.node.message_old.text.parser.TextParser;
import dev.dotspace.network.node.web.AbstractRestController;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/v1/message/old")
@Log4j2
public final class MessageController_okd extends AbstractRestController {


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
  public ResponseEntity<ITextMessage> getFormatted(@RequestBody @NotNull final ImmutableStringContent message,
                                                   @RequestParam(required = false) final String lang) throws InterruptedException {
    final Locale locale = this.localeFromTag(lang);

    return this.validateOkResponse(this.parser.parse(message.message(), locale), "Error while creating.");
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
