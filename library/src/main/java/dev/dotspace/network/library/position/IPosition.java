package dev.dotspace.network.library.position;

/**
 * Position store x,y,z
 */
public interface IPosition {
  /**
   * Get x of position.
   *
   * @return pos x.
   */
  long x();

  /**
   * Get y of position.
   *
   * @return pos y.
   */
  long y();

  /**
   * Get z of position.
   *
   * @return pos z.
   */
  long z();
}
