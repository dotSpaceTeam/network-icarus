package dev.dotspace.network.library.profile.attribute;

import dev.dotspace.network.library.key.IKey;
import dev.dotspace.network.library.profile.ImmutableProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 * Also see {@link IKey}.
 */
//Swagger
@Schema(implementation=ImmutableProfileAttribute.class)
public interface IProfileAttribute extends IKey {
  /**
   * Value of attribute.
   *
   * @return value of attribute.
   */
  //Swagger
  @Schema(example="value", description="Value of attribute.")
  @NotNull String value();
}
