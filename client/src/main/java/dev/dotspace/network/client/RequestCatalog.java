package dev.dotspace.network.client;

import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public final class RequestCatalog {
  /**
   * Create new request instance.
   *
   * @return instance of {@link IProfileManipulator}.
   */
  public static @NotNull IProfileManipulator profileRequest() {
    return new ProfileRequest();
  }

  /**
   * Create new request instance.
   *
   * @return instance of {@link IPositionManipulator}.
   */
  public static @NotNull IPositionManipulator positionRequest() {
    return new PositionRequest();
  }
}