package dev.dotspace.network.library.spigot.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.dotspace.network.library.game.profile.ISkin;
import dev.dotspace.network.library.game.profile.SkinOrigin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@Accessors(fluent = true)
public final class Skin implements ISpigotSkin {
    /**
     * See {@link ISkin#value()}
     */
    @Getter
    private final @NotNull String value;
    /**
     * See {@link ISkin#origin()}
     */
    @Getter
    private final @NotNull SkinOrigin origin;

    /**
     * Construct instance.
     *
     * @param value  of skin information.
     * @param origin get value from name or url of texture (base64 encoded)
     */
    public Skin(@Nullable final String value,
                @Nullable final SkinOrigin origin) {
        this.value = Objects.requireNonNull(value);
        this.origin = Objects.requireNonNull(origin);
    }

    /**
     * See {@link ISkin#createProfile()}
     */
    @Override
    public @NotNull PlayerProfile createProfile() {//Create profile with name
        final boolean name = this.origin == SkinOrigin.PROFILE_NAME; //True if skin is set trough name
        final PlayerProfile playerProfile = Bukkit
                .createProfile(name ? this.value() : UUID.randomUUID().toString().split("-")[0] /*Gen rmd.*/);

        //Set value to profile if origin is TEXTURE_VALUE
        if (!name) {
            this.textureProperty(playerProfile);
        }

        return playerProfile;
    }

    /**
     * See {@link ISkin#attachProfile(Object)}
     */
    @Override
    public void attachProfile(@Nullable final PlayerProfile playerProfile) {
        //Null check
        Objects.requireNonNull(playerProfile);

        //Throw error
        if (this.origin == SkinOrigin.PROFILE_NAME) {
            throw new IllegalStateException( /* Error message */
                    "Skin value is meant to be used for name. Create new profile or use ISkin#createProfile instead.");
        }

        //Set content.
        this.textureProperty(playerProfile);
    }

    /**
     * Set textures for player.
     *
     * @param playerProfile to set textures for.
     */
    private void textureProperty(@NotNull final PlayerProfile playerProfile) {
        playerProfile.setProperty(new ProfileProperty("textures", this.value()));
    }
}
