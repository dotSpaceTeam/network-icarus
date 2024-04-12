package dev.dotspace.network.library.prohibit;

import dev.dotspace.network.library.profile.IProfile;
import jakarta.validation.constraints.NotNull;


public interface IProhibitLayout {
  /**
   * Get reason for prohibit.
   *
   * @return reason for prohibit. Name has to match {@link IProhibitReason#name()}.
   */
  @NotNull String reason();

  /**
   * Get profile to punish.
   *
   * @return punish profile. UniqueId of {@link IProfile#uniqueId()}
   */
  @NotNull String punishedProfile();

  /**
   * Get profile executed.
   *
   * @return executed profile. UniqueId of {@link IProfile#uniqueId()}
   */
  @NotNull String executorProfile();

  /**
   * Custom message of prohibit.
   *
   * @return message for ban.
   */
  @NotNull String message();
}
