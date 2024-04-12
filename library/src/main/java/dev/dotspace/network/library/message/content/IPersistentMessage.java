package dev.dotspace.network.library.message.content;

import dev.dotspace.network.library.key.IKey;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.ImmutableMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * Store message under key and local.
 */
//Swagger
@Schema(implementation=ImmutablePersistentMessage.class)
public interface IPersistentMessage extends IMessage, IKey {
  /**
   * Locale of message.
   *
   * @return locale as {@link Locale}.
   */
  //Swagger
  @Schema(
      example="de-DE",
      description="Language to store message in.",
      implementation=String.class
  )
  @NotNull Locale locale();
}
