package dev.dotspace.network.library.locale;

import dev.dotspace.network.library.message.ImmutableMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 *  Store locale
 */
//Swagger
@Schema(implementation= ImmutableMessage.class)
public interface ILocale {
  /**
   * Tag of locale
   *
   * @return Tag to identify locale.
   */
  //Swagger
  @Schema(
      example="de-DE",
      description="Store locale"
  )
  @NotNull String locale();
}
