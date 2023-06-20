package dev.dotspace.network.client;

import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import org.jetbrains.annotations.NotNull;

public interface IClient {
  /**
   * Create new request instance.
   *
   * @return instance of {@link IProfileManipulator}.
   */
  @NotNull IProfileManipulator profileRequest();

  /**
   * Create new request instance.
   *
   * @return instance of {@link IPositionManipulator}.
   */
  @NotNull IPositionManipulator positionRequest();
}
