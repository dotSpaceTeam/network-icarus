package dev.dotspace.network.library.position;

/**
 * Base of {@link IPosition} and also yaw, pitch
 */
public interface IViewPosition extends IPosition {
  /**
   * Get x of position.
   *
   * @return pos x.
   */
  long yaw();

  /**
   * Get y of position.
   *
   * @return pos y.
   */
  long pitch();
}
