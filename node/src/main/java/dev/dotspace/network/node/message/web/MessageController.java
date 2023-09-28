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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.log4j.Log4j2;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


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
   * Get a formatted message.
   */
  @PostMapping("/")
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
                                              @RequestParam(required=false) final String lang) throws ElementException {
    //Get message or default
    final Locale locale = this.localeFromTag(lang);
    //Response to client.
    return ResponseEntity.ok(ImmutableMessage.of(this.message(locale, message.message())));
  }

  /**
   * Get an profile from unique id.
   */
  @PutMapping("/key/")
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
                                                   @RequestParam(required=false, defaultValue="false") final boolean createOnly) throws ElementException {
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
      throws ElementException {
    //Get message or default
    Locale locale = this.localeFromTag(lang);
    //Get stored message.
    final IPersistentMessage persistentMessage = this.messageDatabase.message(locale, key, findAny);

    //Update locale to message locale.
    locale = persistentMessage.locale();

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
  private @NotNull Locale localeFromTag(@Nullable final String lang) {
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
              .message(locale, key, false);

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
