package dev.dotspace.network.library.profile;

import dev.dotspace.common.response.CompletableResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IProfileManipulator {
  @NotNull CompletableResponse<IProfile> createProfile(@Nullable final String uniqueId,
                                                       @Nullable final ProfileType profileType);

  /**
   * Get profile of uniqueId.
   * Null if no client is present or uniqueId is null.
   *
   * @param uniqueId to pull profile for.
   * @return response with {@link IProfile}.
   */
  @NotNull CompletableResponse<IProfile> getProfile(@Nullable final String uniqueId);


  @NotNull CompletableResponse<? extends List<? extends IProfileAttribute>> getAttributes(@Nullable final String uniqueId);

  @NotNull CompletableResponse<IProfileAttribute> getAttribute(@Nullable final String uniqueId,
                                                               @Nullable final String key);

  @NotNull CompletableResponse<IProfileAttribute> setAttribute(@Nullable final String uniqueId,
                                                               @Nullable final String key,
                                                               @Nullable final String value);

  @NotNull CompletableResponse<IProfileAttribute> removeAttribute(@Nullable final String uniqueId,
                                                                  @Nullable final String key);
}
