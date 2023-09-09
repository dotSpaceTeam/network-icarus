package dev.dotspace.network.library.profile.experience;

import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IExperienceManipulator {

  @NotNull Response<IExperience> addExperience(@Nullable final String uniqueId,
                                               @Nullable final String experienceName,
                                               final long value);

  @NotNull Response<IExperience> getExperience(@Nullable final String uniqueId,
                                               @Nullable final String experienceName);

  @NotNull Response<IExperience> getTotalExperience(@Nullable final String uniqueId);

}

