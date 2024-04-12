package dev.dotspace.network.client.rest.profile;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.session.IPlaytime;
import dev.dotspace.network.library.profile.session.ISession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Request for {@link IProfile}.
 */
public interface IProfileRequest {
  @NotNull Response<IProfile> updateProfile(@Nullable final String uniqueId,
                                            @Nullable final String name,
                                            @Nullable final ProfileType profileType);

  /**
   * Get profile of uniqueId.
   * Null if no client is present or uniqueId is null.
   *
   * @param uniqueId to pull profile for.
   * @return response with {@link IProfile}.
   */
  @NotNull Response<IProfile> getProfile(@Nullable final String uniqueId);

  /**
   * Get profile of name.
   * Null if no client is present or name is null.
   *
   * @param name to pull profile for.
   * @return response with {@link IProfile}.
   */
  @NotNull Response<IProfile> getProfileFromName(@Nullable final String name);

  @NotNull Response<? extends List<? extends IProfileAttribute>> getAttributeList(@Nullable final String uniqueId);

  @NotNull Response<IProfileAttribute> getAttribute(@Nullable final String uniqueId,
                                                    @Nullable final String key);

  @NotNull Response<IProfileAttribute> setAttribute(@Nullable final String uniqueId,
                                                    @Nullable final String key,
                                                    @Nullable final String value);

  @NotNull Response<IProfileAttribute> removeAttribute(@Nullable final String uniqueId,
                                                       @Nullable final String key);

  /**
   * Get Session from id and profile.
   *
   * @param uniqueId  to get specific session.
   * @param sessionId id of session.
   * @return matching {@link ISession} of profileId and sessionId.
   */
  @NotNull Response<ISession> getSession(@Nullable final String uniqueId,
                                         @Nullable final Long sessionId);

  /**
   * Calculate playtime of profile id.
   *
   * @param uniqueId to calculate playtime for.
   * @return calculated play time of profile.
   */
  @NotNull Response<IPlaytime> getPlaytime(@Nullable final String uniqueId);

  /**
   * Create new session for profile.
   *
   * @param uniqueId to create new session for.
   * @return created {@link ISession}.
   */
  @NotNull Response<ISession> createSession(@Nullable final String uniqueId,
                                            @Nullable final String address);

  /**
   * Complete a session of profile.
   *
   * @param uniqueId  to complete session for.
   * @param sessionId to complete.
   * @return completed {@link ISession}.
   */
  @NotNull Response<ISession> completeSession(@Nullable final String uniqueId,
                                              @Nullable final Long sessionId);

  /**
   * Get total experience of player.
   *
   * @param uniqueId to get experience for.
   * @return completed {@link IExperience}.
   */
  @NotNull Response<IExperience> getTotalExperience(@Nullable final String uniqueId);

  /**
   * Get specific experience of player.
   *
   * @param uniqueId profile to get experience from.
   * @param name     of experience to get.
   * @return completed {@link IExperience}.
   */
  @NotNull Response<IExperience> getExperience(@Nullable final String uniqueId,
                                               @Nullable final String name);

  /**
   * Add specific experience to player.
   *
   * @param uniqueId   profile to add experience to.
   * @param name       of experience to add points.
   * @param experience to add
   * @return completed {@link IExperience}.
   */
  @NotNull Response<IExperience> addExperience(@Nullable final String uniqueId,
                                               @Nullable final String name,
                                               final long experience);

  /**
   * Add economy to player.
   *
   * @param uniqueId to add money.
   * @param currency name of currency.
   * @param amount   to add.
   * @return completed {@link ITransaction}.
   */
  @NotNull Response<ITransaction> deposit(@Nullable final String uniqueId,
                                          @Nullable final String currency,
                                          final int amount);

  /**
   * Remove economy to player.
   *
   * @param uniqueId to remove money.
   * @param currency name of currency.
   * @param amount   to remove.
   * @return completed {@link ITransaction}.
   */
  @NotNull Response<ITransaction> withdraw(@Nullable final String uniqueId,
                                           @Nullable final String currency,
                                           final int amount);
}
