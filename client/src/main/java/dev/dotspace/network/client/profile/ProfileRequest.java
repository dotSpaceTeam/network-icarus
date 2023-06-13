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
public final class ProfileRequest implements IProfileManipulator {

  @Autowired
  private IClient client;

  /**
   * See {@link IProfileManipulator#getProfile(String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfile> getProfile(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client.get("/v1/profile/%s".formatted(uniqueId), ImmutableProfile.class);
    });
  }

  /**
   * See {@link IProfileManipulator#createProfile(String, ProfileType)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfile> createProfile(@Nullable String uniqueId,
                                                              @Nullable ProfileType profileType) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(profileType);

      return this.client.put("/v1/profile", ImmutableProfile.class, new ImmutableProfile(uniqueId, profileType));
    });
  }

  /**
   * See {@link IProfileManipulator#getAttributes(String)}.
   */
  @Override
  public @NotNull CompletableResponse<AttributeList> getAttributes(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client.get("/v1/profile/%s/attributes".formatted(uniqueId),
        AttributeList.class);
    });
  }

  /**
   * See {@link IProfileManipulator#getAttribute(String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfileAttribute> getAttribute(@Nullable String uniqueId,
                                                                      @Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      return this.client.get("/v1/profile/%s/attributes/%s".formatted(uniqueId, key), ImmutableProfileAttribute.class);
    });
  }

  @Override
  public @NotNull CompletableResponse<IProfileAttribute> setAttribute(@Nullable String uniqueId,
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
  public @NotNull CompletableResponse<IProfileAttribute> removeAttribute(@Nullable String uniqueId,
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
