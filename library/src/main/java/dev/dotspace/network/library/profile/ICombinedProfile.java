package dev.dotspace.network.library.profile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Combined profile of {@link IProfile} and list of {@link IProfileAttribute}.
 */
public interface ICombinedProfile extends IProfile {
  /**
   * Get attributes of profile.
   *
   * @return list of attributes.
   */
  @NotNull List<IProfileAttribute> attributes();

}
