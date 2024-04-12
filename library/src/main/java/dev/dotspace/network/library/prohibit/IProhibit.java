package dev.dotspace.network.library.prohibit;

import dev.dotspace.network.library.profile.IProfile;
import jakarta.validation.constraints.NotNull;


public interface IProhibit {
  /**
   * Return reason for prohibit.
   *
   * @return present {@link IProhibitReason} responsible for this prohibit.
   */
  @NotNull IProhibitReason reason();

  /**
   * Profile that is affected by this prohibit.
   *
   * @return profile instance.
   */
  @NotNull IProfile punishedProfile();

  @NotNull IProfile executorProfile();

  @NotNull String message();

  boolean active();
}