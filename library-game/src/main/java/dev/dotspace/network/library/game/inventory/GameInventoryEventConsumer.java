package dev.dotspace.network.library.game.inventory;

import org.jetbrains.annotations.NotNull;


/**
 * Consume event close
 *
 * @param <EVENT> generic type of closer.
 */
public interface GameInventoryEventConsumer<EVENT> {
  /**
   * Accept close of consumer
   *
   * @param event triggered consumer.
   * @throws Throwable error while process.
   */
  void accept(@NotNull final EVENT event) throws Throwable;
}
