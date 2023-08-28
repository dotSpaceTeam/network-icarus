package dev.dotspace.network.library.game.util;

import org.jetbrains.annotations.NotNull;


public interface GameCountdown {
  /**
   * Decrement countdown.
   *
   * @return new value of game.
   */
  long tick();

  /**
   * Get current value of countdown
   *
   * @return value.
   */
  long value();

  /**
   * Start value and reset value.
   *
   * @return value
   */
  long startValue();

  /**
   * Reset value to startValue.
   *
   * @return new value.
   */
  long reset();

  /**
   * Check if countdown is done already.
   *
   * @return true if {@link GameCountdown#tick()} reaches 0.
   */
  boolean done();

  /**
   * Set value and startValue to new value.
   */
  @NotNull GameCountdown set(final long value);

  /**
   * Will be called if countdown reaches 0 after calling {@link GameCountdown#tick()}
   */
  void onEnd();

}
