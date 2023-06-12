package dev.dotspace.network.node.profile.db;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.IProfileAttribute;
import dev.dotspace.network.library.profile.ProfileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IProfileDatabase {

  @NotNull CompletableResponse<IProfile> createProfile(@Nullable final String uniqueId,
                                                       @Nullable final ProfileType profileType);

  @NotNull CompletableResponse<IProfile> getProfile(@Nullable final String uniqueId);


  @NotNull CompletableResponse<IProfileAttribute> setAttribute(@Nullable final String uniqueId,
                                                     @Nullable final String key,
                                                     @Nullable final String value);

  @NotNull CompletableResponse<IProfileAttribute> removeAttribute(@Nullable final String uniqueId,
                                                        @Nullable final String key);

  @NotNull CompletableResponse<List<IProfileAttribute>> getAttributes(@Nullable final String uniqueId);

  @NotNull CompletableResponse<IProfileAttribute> getAttribute(@Nullable final String uniqueId,
                                                                     @Nullable final String key);
}
