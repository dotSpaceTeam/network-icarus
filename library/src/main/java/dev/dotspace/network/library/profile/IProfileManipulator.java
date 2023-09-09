package dev.dotspace.network.library.profile;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IProfileManipulator {
  @NotNull Response<IProfile> createProfile(@Nullable final String uniqueId,
                                            @Nullable final ProfileType profileType);

  /**
   * Get profile of uniqueId.
   * Null if no client is present or uniqueId is null.
   *
   * @param uniqueId to pull profile for.
   * @return response with {@link IProfile}.
   */
  @NotNull Response<IProfile> getProfile(@Nullable final String uniqueId);


  @NotNull Response<? extends List<? extends IProfileAttribute>> getAttributes(@Nullable final String uniqueId);

  @NotNull Response<IProfileAttribute> getAttribute(@Nullable final String uniqueId,
                                                               @Nullable final String key);

  @NotNull Response<IProfileAttribute> setAttribute(@Nullable final String uniqueId,
                                                               @Nullable final String key,
                                                               @Nullable final String value);

  @NotNull Response<IProfileAttribute> removeAttribute(@Nullable final String uniqueId,
                                                                  @Nullable final String key);
}
