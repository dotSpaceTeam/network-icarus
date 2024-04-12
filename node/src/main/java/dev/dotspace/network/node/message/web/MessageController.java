package dev.dotspace.network.node.message.web;

import dev.dotspace.network.library.data.ImmutableListObject;
import dev.dotspace.network.library.locale.ILocale;
import dev.dotspace.network.library.locale.ImmutableLocale;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.ImmutableMessage;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.content.ImmutablePersistentMessage;
import dev.dotspace.network.node.database.exception.EntityException;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import dev.dotspace.network.node.message.MessageService;
import dev.dotspace.network.node.message.db.PersistentMessageDatabase;
import dev.dotspace.network.node.web.controller.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.LocaleUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/v1/message")
@Log4j2
//Swagger
@Tag(name="Message Endpoint", description="Manipulate, format and get messages.")
public final class MessageController extends AbstractRestController {
  /**
   * Database to handle.
   */
  @Autowired
  private PersistentMessageDatabase messageDatabase;
  /**
   * Message manger
   */
  @Autowired
  private MessageService messageService;

  /**
   * Get a formatted message.
   */
  @PostMapping
  @ResponseBody
  //Swagger
  @Operation(
      summary="Format message.",
      description="Format message. Replace keys ins messages.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Returns formatted message.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableMessage.class)
                  )
              })
      })
  public ResponseEntity<IMessage> postMessage(@RequestBody @NotNull final ImmutableMessage message,
                                              @RequestParam(required=false) final String lang) throws EntityException {
    //Get message or default
    final Locale locale = this.localeFromTag(lang);
    //Response to client.
    return ResponseEntity.ok(ImmutableMessage.of(this.messageService.complete(locale, message.message())));
  }

  /**
   * Get list of locales.
   */
  @GetMapping("/locale")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all present locale codes.",
      description="Return a list of all codes.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return distinct language codes.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableListObject.class)
                  )
              })
      })
  public ResponseEntity<ImmutableListObject<String>> getLocaleList() {
    return ResponseEntity.ok(this.messageDatabase.getLocaleList());
  }

  /**
   * Get list of locales.
   */
  @PostMapping("/locale/{locale}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all present locale codes.",
      description="Return a list of all codes.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return distinct language codes.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableListObject.class)
                  )
              })
      })
  public ResponseEntity<ILocale> validateLocale(@PathVariable @NotNull final String locale) throws EntityNotPresentException {
    //Get locale instance.
    final Locale instance = Locale.forLanguageTag(locale);

    //Check if present -> if not official, not present (empty.)
    if (!LocaleUtils.isAvailableLocale(instance) ||
        !instance.toLanguageTag().contains("-")) {
      //Error
      throw new EntityNotPresentException("No locale tag="+locale+" present.");
    }

    //Otherwise.
    return ResponseEntity.ok(new ImmutableLocale(instance.toLanguageTag()));
  }

  /**
   * Get an profile from unique id.
   */
  @PutMapping("/key")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Insert a new message to system.",
      description="Create a brand new message, if you like to update one, use POST mapping.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Returns stored message.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutablePersistentMessage.class)
                  )
              })
      })
  public ResponseEntity<IPersistentMessage> putKey(@RequestBody @NotNull final ImmutablePersistentMessage message,
                                                   @RequestParam(required=false, defaultValue="false") final boolean createOnly) throws EntityException {
    //Get create only.
    if (createOnly) {
      //Insert message.
      return ResponseEntity.ok(this.messageDatabase
          .insertMessage(message.locale(), message.key(), message.message()));
    }

    //Update message.
    return ResponseEntity.ok(this.messageDatabase
        .updateMessage(message.locale(), message.key(), message.message()));
  }

  /**
   * Get an profile from unique id.
   */
  @GetMapping("/key/{key}/{lang}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get a present stored message.",
      description="Read a already present message, from system.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Returns stored message. If no lang param present, system is using Nodes default language.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutablePersistentMessage.class)
                  )
              })
      })
  public ResponseEntity<IPersistentMessage> getKey(@PathVariable @NotNull final String key,
                                                   @PathVariable @NotNull final String lang,
                                                   @RequestParam(required=false, defaultValue="false") final boolean findAny)
      throws EntityException {
    //Get message or default
    Locale locale = this.localeFromTag(lang);
    //Get stored message.
    final IPersistentMessage persistentMessage = this.messageDatabase.message(locale, key, findAny);

    //Update locale to message locale.
    locale = persistentMessage.locale();

    //Return value
    return ResponseEntity.ok(
        new ImmutablePersistentMessage(key, locale, this.messageService.complete(locale, persistentMessage.message())));
  }

  /**
   * Convert {@link String} with lang tag to {@link Locale}.
   *
   * @param lang to convert.
   * @return present locale otherwise using.
   */
  private @NotNull Locale localeFromTag(@Nullable final String lang) {
    return Optional
        .ofNullable(lang)
        .map(s -> s.replaceAll("_", "-"))
        //Convert string to locale tag.
        .map(Locale::forLanguageTag)
        .orElseGet(Locale::getDefault);
  }
}
