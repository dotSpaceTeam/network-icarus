package dev.dotspace.network.library.spigot.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.dotspace.network.library.game.profile.AbstractGameSkin;
import dev.dotspace.network.library.game.profile.GameSkin;
import dev.dotspace.network.library.game.profile.SkinOrigin;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Accessors(fluent=true)
public final class Skin extends AbstractGameSkin<PlayerProfile> implements ISkin {

  /**
   * Construct instance.
   *
   * @param value     of skin information.
   * @param signature of skin.
   * @param origin    get value from name or url of texture (base64 encoded)
   */
  public Skin(@Nullable String value,
              @Nullable String signature,
              @Nullable SkinOrigin origin) {
    super(value, signature, origin);
  }

  /**
   * See {@link GameSkin#createProfile()}
   */
  @Override
  public @NotNull PlayerProfile createProfile() {//Create profile with name
    final boolean name = this.origin() == SkinOrigin.PROFILE_NAME; //True if skin is set trough name
    final PlayerProfile playerProfile = Bukkit.createProfile(name ? this.value() : this.randomName() /*Gen rmd.*/);

    //Set value to profile if origin is TEXTURE_VALUE
    if (!name) {
      this.textureProperty(playerProfile);
    }

    return playerProfile;
  }

  /**
   * Set textures for player.
   *
   * @param playerProfile to set textures for.
   */
  private void textureProperty(@NotNull final PlayerProfile playerProfile) {
    this.signature()
        //Create with signature
        .ifPresentOrElse(s -> playerProfile.setProperty(new ProfileProperty("textures", this.value())),
            //Create without signature
            () -> playerProfile.setProperty(new ProfileProperty("textures", this.value())));
  }
}
