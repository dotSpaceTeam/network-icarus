package dev.dotspace.network.library.game.inventory;

import org.jetbrains.annotations.NotNull;


/**
 * Handle open.
 *
 * @param <INVENTORY> generic type of inventory.
 * @param <PLAYER> generic type of player.
 */
public interface GameInventoryOpenConsumer<INVENTORY, PLAYER> {
  /**
   * Handle open of inventory.
   *
   * @param inventory that was opened.
   * @param player    that opened inventory.
   * @throws Throwable error while process.
   */
  void accept(@NotNull final INVENTORY inventory,
              @NotNull final PLAYER player) throws Throwable;
}
