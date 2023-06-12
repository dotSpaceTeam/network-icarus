package dev.dotspace.network.library.position;

import dev.dotspace.network.library.key.IKey;

/**
 * Position store x,y,z
 */
public interface IPosition extends IKey {
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
