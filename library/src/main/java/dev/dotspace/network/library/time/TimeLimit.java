package dev.dotspace.network.library.time;

import jakarta.validation.constraints.NotNull;

import java.util.Date;


/**
 *
 */
public interface TimeLimit {
  /**
   * True if expired. (Expire date must be present, and be positive).
   *
   * @return true if time was reached.
   */
  boolean expired();

  /**
   * Date of expire. (Date with {@link Long#MAX_VALUE} is used for lifetime.)
   *
   * @return stored date.
   */
  @NotNull Date expireDate();
}
