package dev.dotspace.network.client;

import dev.dotspace.network.client.status.IStatusRequest;
import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import dev.dotspace.network.library.session.ISessionManipulator;
import org.jetbrains.annotations.NotNull;

public interface IClient {
  /**
   * Get status of api point.
   *
   * @return instance of {@link IStatusRequest}.
   */
  @NotNull IStatusRequest statusRequest();

  /**
   * Get request instance of client.
   *
   * @return instance of {@link IProfileManipulator}.
   */
  @NotNull IProfileManipulator profileRequest();

  /**
   * Get request instance of client.
   *
   * @return instance of {@link IPositionManipulator}.
   */
  @NotNull IPositionManipulator positionRequest();

  /**
   * Get request instance of client.
   *
   * @return instance of {@link ISessionManipulator}.
   */
  @NotNull ISessionManipulator sessionRequest();
}