package dev.dotspace.network.client.profile;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.IProfileAttribute;
import dev.dotspace.network.library.profile.ProfileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IProfileRequest {
  /**
   * Get profile of uniqueId.
   * Null if no client is present or uniqueId is null.
   *
   * @param uniqueId to pull profile for.
   * @return response with {@link IProfile}.
   */
  @NotNull CompletableResponse<IProfile> getProfile(@Nullable final String uniqueId);

  @NotNull CompletableResponse<IProfile> insertProfile(@Nullable final String uniqueId,
                                                       @Nullable final ProfileType profileType);

  @NotNull CompletableResponse<AttributeList> getProfileAttributes(@Nullable final String uniqueId);

  @NotNull CompletableResponse<IProfileAttribute> getProfileAttribute(@Nullable final String uniqueId,
                                                                      @Nullable final String key);

  @NotNull CompletableResponse<IProfileAttribute> setProfileAttribute(@Nullable final String uniqueId,
                                                                      @Nullable final String key,
                                                                      @Nullable final String value);

  @NotNull CompletableResponse<IProfileAttribute> removeProfileAttribute(@Nullable final String uniqueId,
                                                                         @Nullable final String key);
}
