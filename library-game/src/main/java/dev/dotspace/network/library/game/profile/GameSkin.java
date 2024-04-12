package dev.dotspace.network.library.game.profile;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;


/**
 * Manipulate skin information.
 *
 * @param <PROFILE> generic type of implementation.
 */
public interface GameSkin<PROFILE> {
  /**
   * Value of skin.
   *
   * @return value for skin
   */
  @NotNull String value();

  @NotNull Optional<String> signature();

  /**
   * Type of skin. (See {@link SkinOrigin})
   *
   * @return type.
   */
  @NotNull SkinOrigin origin();

  /**
   * Create profile of information. if {@link SkinOrigin#TEXTURE_VALUE} a random name will be generated.
   *
   * @return profile of skin.
   */
  @NotNull PROFILE createProfile();
}
