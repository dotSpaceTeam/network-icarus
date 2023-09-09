package dev.dotspace.network.client.profile;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.key.ImmutableKey;
import dev.dotspace.network.library.profile.*;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public final class ProfileRequest extends AbstractRequest implements IProfileRequest {
  /**
   * See {@link IProfileManipulator#getProfile(String)}.
   */
  @Override
  public @NotNull Response<IProfile> getProfile(@Nullable String uniqueId) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(uniqueId);

          return this.client().get("/v1/profile/%s".formatted(uniqueId), ImmutableProfile.class);
        });
  }

  /**
   * See {@link IProfileManipulator#createProfile(String, ProfileType)}.
   */
  @Override
  public @NotNull Response<IProfile> createProfile(@Nullable String uniqueId,
                                                   @Nullable ProfileType profileType) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(uniqueId);
          Objects.requireNonNull(profileType);

          return this.client().put("/v1/profile", ImmutableProfile.class, new ImmutableProfile(uniqueId, profileType));
        });
  }

  /**
   * See {@link IProfileManipulator#getAttributes(String)}.
   */
  @Override
  public @NotNull Response<AttributeList> getAttributes(@Nullable String uniqueId) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(uniqueId);

          return this.client().get("/v1/profile/%s/attributes".formatted(uniqueId), AttributeList.class);
        });
  }

  /**
   * See {@link IProfileManipulator#getAttribute(String, String)}.
   */
  @Override
  public @NotNull Response<IProfileAttribute> getAttribute(@Nullable String uniqueId,
                                                           @Nullable String key) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(uniqueId);
          Objects.requireNonNull(key);

          return this.client().get("/v1/profile/%s/attributes/%s".formatted(uniqueId, key), ImmutableProfileAttribute.class);
        });
  }

  @Override
  public @NotNull Response<IProfileAttribute> setAttribute(@Nullable String uniqueId,
                                                           @Nullable String key,
                                                           @Nullable String value) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(uniqueId);
          Objects.requireNonNull(key);
          Objects.requireNonNull(value);

          return this.client().post("/v1/profile/%s/attributes".formatted(uniqueId),
              ImmutableProfileAttribute.class,
              new ImmutableProfileAttribute(key, value));
        });
  }

  @Override
  public @NotNull Response<IProfileAttribute> removeAttribute(@Nullable String uniqueId,
                                                              @Nullable String key) {
    return this
        .responseService()
        .response(() -> {
          //Null check
          Objects.requireNonNull(uniqueId);
          Objects.requireNonNull(key);

          return this.client().delete("/v1/profile/%s/attributes".formatted(uniqueId),
              ImmutableProfileAttribute.class,
              new ImmutableKey(key));
        });
  }
}
