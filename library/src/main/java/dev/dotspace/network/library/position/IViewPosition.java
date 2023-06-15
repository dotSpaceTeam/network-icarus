package dev.dotspace.network.library.position;

/**
 * Extended {@link IPosition} with additional yaw and pitch parameters.
 */
public interface IViewPosition extends IPosition {
  /**
   * Get yaw of ViewPosition.
   *
   * @return yaw
   */
  long yaw();

  /**
   * Get pitch of ViewPosition.
   *
   * @return pitch
   */
  long pitch();
}
