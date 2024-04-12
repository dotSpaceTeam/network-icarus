package dev.dotspace.network.library.velocity.profile;

import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import dev.dotspace.network.library.game.profile.AbstractGameSkin;
import dev.dotspace.network.library.game.profile.SkinOrigin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


public final class Skin extends AbstractGameSkin<GameProfile> implements ISkin {
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

  @Override
  public @NotNull GameProfile createProfile() {
    //True if skin is set trough name
    if (this.origin() == SkinOrigin.PROFILE_NAME) {
      //Return name.
      return new GameProfile(UUID.randomUUID(), this.value(), new ArrayList<>());
    }

    //Create profile.
    final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), this.randomName(), new ArrayList<>());

    //Create with signature
    gameProfile.addProperty(new Property("textures", this.value(), this.signature().orElseGet(this::value)));

    return gameProfile;
  }

  //static
  public static @NotNull Optional<ISkin> fromProfile(@Nullable final GameProfile gameProfile) {
    //Null check
    Objects.requireNonNull(gameProfile);

    //Loop trough every profile
    for (GameProfile.Property property : gameProfile.getProperties()) {
      //Next if not texture.
      if (!property.getName().equalsIgnoreCase("textures")) {
        continue;
      }
      //Create instance.
      return Optional.of(new Skin(property.getValue(), property.getSignature(), SkinOrigin.TEXTURE_VALUE));
    }
    //Null
    return Optional.empty();
  }
}
