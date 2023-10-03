package dev.dotspace.network.node.message;

import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.message.parser.MessageParser;
import dev.dotspace.network.node.message.db.PersistentMessageDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Component to handle all messages.
 */
@Component
public final class MessageService {
  /**
   * Database to handle.
   */
  @Autowired
  private PersistentMessageDatabase messageDatabase;

  public @NotNull String complete(@NotNull final Locale locale,
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
