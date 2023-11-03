package dev.dotspace.network.client.position;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IViewPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Request to modify {@link IPosition} and {@link IViewPosition}.
 */
public interface IPositionRequest {
  /**
   * Insert or update {@link IPosition}.
   *
   * @param key to set position for.
   * @param x   see {@link IPosition#x()}.
   * @param y   see {@link IPosition#y()}.
   * @param z   see {@link IPosition#z()}.
   * @return updated {@link IPosition}.
   */
  @NotNull Response<IPosition> setPosition(@Nullable final String key,
                                           final long x,
                                           final long y,
                                           final long z);

  /**
   * Get position.
   *
   * @param key to get position from, null if absent.
   * @return present {@link IPosition}.
   */
  @NotNull Response<IPosition> getPosition(@Nullable final String key);

  /**
   * Insert or update {@link IViewPosition}.
   *
   * @param key   to set position for.
   * @param x     see {@link IPosition#x()}.
   * @param y     see {@link IPosition#y()}.
   * @param z     see {@link IPosition#z()}.
   * @param yaw   see {@link IViewPosition#yaw()}.
   * @param pitch see {@link IViewPosition#pitch()}.
   * @return updated {@link IViewPosition}.
   */
  @NotNull Response<IViewPosition> setViewPosition(@Nullable final String key,
                                                   final long x,
                                                   final long y,
                                                   final long z,
                                                   final long yaw,
                                                   final long pitch);

  /**
   * Get viewPosition.
   *
   * @param key to get position from, null if absent.
   * @return present {@link IViewPosition}.
   */
  @NotNull Response<IViewPosition> getViewPosition(@Nullable final String key);
}
