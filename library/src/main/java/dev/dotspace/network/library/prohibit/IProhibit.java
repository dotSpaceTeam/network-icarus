package dev.dotspace.network.library.prohibit;

import dev.dotspace.network.library.profile.IProfile;
import jakarta.validation.constraints.NotNull;


public interface IProhibit {
  @NotNull IProhibitReason reason();

  @NotNull IProfile punishedProfile();

  @NotNull IProfile executorProfile();

  @NotNull String customMessage();

  boolean active();
}