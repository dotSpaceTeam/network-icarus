package dev.dotspace.network.library.game.inventory;

import org.jetbrains.annotations.NotNull;

/**
 * Handle click consumer
 *
 * @param <ITEM> generic type of click item.
 * @param <EVENT> event.
 */
public interface GameInventoryClickConsumer<ITEM, EVENT> {
  /**
   * Accept click of consumer
   *
   * @param event triggered consumer.
   * @param item  clicked.
   * @param slot  of clicked item.
   * @throws Throwable error while process, if town event will be canceled.
   */
  void accept(@NotNull final EVENT event,
              @NotNull final ITEM item,
              final int slot) throws Throwable;
}
