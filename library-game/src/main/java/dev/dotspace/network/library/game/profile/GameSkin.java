package dev.dotspace.network.library.game.profile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Attach skin data to profile.
     *
     * @param profile to attach skin data
     * @throws IllegalStateException if origin is set to {@link SkinOrigin#PROFILE_NAME}. A new profile is needed.
     */
    void attachProfile(@Nullable final PROFILE profile);
}
