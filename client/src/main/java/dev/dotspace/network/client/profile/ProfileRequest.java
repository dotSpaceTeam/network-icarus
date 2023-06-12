package dev.dotspace.network.client.profile;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.client.web.IClient;
import dev.dotspace.network.library.key.ImmutableKey;
import dev.dotspace.network.library.profile.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class ProfileRequest implements IProfileRequest {

  @Autowired
  private IClient client;

  /**
   * See {@link IProfileRequest#getProfile(String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfile> getProfile(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client.get("/v1/profile/%s".formatted(uniqueId), ImmutableProfile.class);
    });
  }

  @Override
  public @NotNull CompletableResponse<ICombinedProfile> getCombinedProfile(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client.get("/v1/profile/%s?attributes=true".formatted(uniqueId), ImmutableCombinedProfile.class);
    });
  }

  /**
   * See {@link IProfileRequest#insertProfile(String, ProfileType)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfile> insertProfile(@Nullable String uniqueId,
                                                              @Nullable ProfileType profileType) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(profileType);

      return this.client.put("/v1/profile", ImmutableProfile.class, new ImmutableProfile(uniqueId, profileType));
    });
  }

  /**
   * See {@link IProfileRequest#getProfileAttributes(String)}.
   */
  @Override
  public @NotNull CompletableResponse<AttributeList> getProfileAttributes(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client.get("/v1/profile/%s/attributes".formatted(uniqueId),
        AttributeList.class);
    });
  }

  /**
   * See {@link IProfileRequest#getProfileAttribute(String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfileAttribute> getProfileAttribute(@Nullable String uniqueId,
                                                                             @Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      return this.client.get("/v1/profile/%s/attributes/%s".formatted(uniqueId, key), ImmutableProfileAttribute.class);
    });
  }

  @Override
  public @NotNull CompletableResponse<IProfileAttribute> setProfileAttribute(@Nullable String uniqueId,
                                                                             @Nullable String key,
                                                                             @Nullable String value) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);
      Objects.requireNonNull(value);

      return this.client.post("/v1/profile/%s/attributes".formatted(uniqueId),
        ImmutableProfileAttribute.class,
        new ImmutableProfileAttribute(key, value));
    });
  }

  @Override
  public @NotNull CompletableResponse<IProfileAttribute> removeProfileAttribute(@Nullable String uniqueId,
                                                                                @Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      return this.client.delete("/v1/profile/%s/attributes".formatted(uniqueId),
        ImmutableProfileAttribute.class,
        new ImmutableKey(key));
    });
  }
}
