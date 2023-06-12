package dev.dotspace.network.library.profile;

import org.jetbrains.annotations.NotNull;

public interface IProfile {
    /**
     * UniqueId of player given by mojang and used for system.
     *
     * @return uniqueId as {@link String}.
     */
    @NotNull String uniqueId();

    /**
     * Type of profile.
     *
     * @return type of profile.
     */
    @NotNull ProfileType profileType();
}
