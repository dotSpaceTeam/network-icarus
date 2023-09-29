package dev.dotspace.network.client.profile;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.connection.ImmutableAddressName;
import dev.dotspace.network.library.key.ImmutableKey;
import dev.dotspace.network.library.profile.*;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.library.profile.session.IPlaytime;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.library.profile.session.ImmutablePlaytime;
import dev.dotspace.network.library.profile.session.ImmutableSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public final class ProfileRequest extends AbstractRequest implements IProfileRequest {
  /**
   * See {@link IProfileRequest#updateProfile(String, String, ProfileType)}.
   */
  @Override
  public @NotNull Response<IProfile> updateProfile(@Nullable String uniqueId,
                                                   @Nullable String name,
                                                   @Nullable ProfileType profileType) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(name);
      Objects.requireNonNull(profileType);

      return this.client()
          .put("/api/v1/profile/", ImmutableProfile.class, new ImmutableProfile(uniqueId, name, profileType));
    });
  }

  /**
   * See {@link IProfileRequest#getProfile(String)}.
   */
  @Override
  public @NotNull Response<IProfile> getProfile(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client().get("/api/v1/profile/"+uniqueId, ImmutableProfile.class);
    });
  }


  /**
   * See {@link IProfileRequest#getProfileFromName(String)}.
   */
  @Override
  public @NotNull Response<IProfile> getProfileFromName(@Nullable String name) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(name);

      return this.client().get("/api/v1/profile/"+name+"?nameSearch=true", ImmutableProfile.class);
    });
  }

  /**
   * See {@link IProfileRequest#getAttributeList(String)}.
   */
  @Override
  public @NotNull Response<AttributeList> getAttributeList(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      return this.client().get("/api/v1/profile/"+uniqueId+"/attribute", AttributeList.class);
    });
  }

  /**
   * See {@link IProfileRequest#getAttribute(String, String)}.
   */
  @Override
  public @NotNull Response<IProfileAttribute> getAttribute(@Nullable String uniqueId,
                                                           @Nullable String key) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      return this.client().get("/api/v1/profile/"+uniqueId+"/attribute/"+key,
          ImmutableProfileAttribute.class);
    });
  }

  @Override
  public @NotNull Response<IProfileAttribute> setAttribute(@Nullable String uniqueId,
                                                           @Nullable String key,
                                                           @Nullable String value) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);
      Objects.requireNonNull(value);

      return this.client().post("/api/v1/profile/"+uniqueId+"/attribute/",
          ImmutableProfileAttribute.class,
          new ImmutableProfileAttribute(key, value));
    });
  }

  @Override
  public @NotNull Response<IProfileAttribute> removeAttribute(@Nullable String uniqueId,
                                                              @Nullable String key) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      return this.client().delete("/api/v1/profile/"+uniqueId+"/attribute/",
          ImmutableProfileAttribute.class,
          new ImmutableKey(key));
    });
  }


  /**
   * See {@link IProfileRequest#getSession(String, Long)}.
   */
  @Override
  public @NotNull Response<ISession> getSession(@Nullable String uniqueId,
                                                @Nullable Long sessionId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(sessionId);

      //Send request
      return this.client()
          .get("/api/v1/profile/"+uniqueId+"/session/"+sessionId, ImmutableSession.class);
    });
  }

  /**
   * See {@link IProfileRequest#getPlaytime(String)}.
   */
  @Override
  public @NotNull Response<IPlaytime> getPlaytime(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      //Send request
      return this.client()
          .get("/api/v1/profile/"+uniqueId+"/playtime", ImmutablePlaytime.class);
    });
  }

  /**
   * See {@link IProfileRequest#createSession(String, String)}.
   */
  @Override
  public @NotNull Response<ISession> createSession(@Nullable String uniqueId,
                                                   @Nullable String address) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(address);

      //Send request
      return this.client()
          .post("/api/v1/profile/"+uniqueId+"/session/",
              ImmutableSession.class,
              new ImmutableAddressName(address));
    });
  }

  /**
   * See {@link IProfileRequest#completeSession(String, Long)}.
   */
  @Override
  public @NotNull Response<ISession> completeSession(@Nullable String uniqueId,
                                                     @Nullable Long sessionId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(sessionId);

      //Send request
      return this.client()
          .put("/api/v1/profile/"+uniqueId+"/session/"+sessionId, ImmutableSession.class);
    });
  }

  @Override
  public @NotNull Response<IExperience> getTotalExperience(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      //Send request
      return this.client()
          .get("/api/v1/profile/"+uniqueId+"/experience", ImmutableExperience.class);
    });
  }

  @Override
  public @NotNull Response<IExperience> getExperience(@Nullable String uniqueId,
                                                      @Nullable String name) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(name);

      //Send request
      return this.client()
          .get("/api/v1/profile/"+uniqueId+"/experience/"+name, ImmutableExperience.class);
    });
  }

  @Override
  public @NotNull Response<IExperience> addExperience(@Nullable String uniqueId,
                                                      @Nullable String name,
                                                      long experience) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(name);

      //Check if experience is positive
      if (experience<=0) {
        //Throw error.
        throw new IllegalArgumentException("Error while adding experience. Value must be positive.");
      }

      //Send request
      return this.client()
          .post("/api/v1/profile/"+uniqueId+"/experience/",
              ImmutableExperience.class,
              new ImmutableExperience(name, experience, -1));
    });
  }
}