package dev.dotspace.network.library.game.bridge;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.profile.ProfileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Parameters for basic communication.
 */
public interface IClientMask {
  /**
   * Connect a client.
   *
   * @param profileType
   * @param uniqueId
   * @param name
   * @param texture
   * @return
   */
  @NotNull Response<Boolean> connect(@Nullable final ProfileType profileType,
                                     @Nullable final String uniqueId,
                                     @Nullable final String name,
                                     @Nullable String address,
                                     @Nullable final String texture,
                                     @Nullable final String signature);

  @NotNull Response<Boolean> disconnect(@Nullable final String uniqueId);
}
