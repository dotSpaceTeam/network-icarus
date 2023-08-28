package dev.dotspace.network.library.game.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> {

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> setItem(@Nullable final ITEM item,
                                                                                            final int slot,
                                                                                            @Nullable final GameInventoryClickConsumer<ITEM, CLICK_EVENT> consumer);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> setItem(@Nullable final ITEM item,
                                                                                            final int slot);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> appendItem(@Nullable final ITEM item);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> removeItem(final int slot);

  /*
   * Handles
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> handleClick(final int slot,
                                                                                                @Nullable final GameInventoryClickConsumer<ITEM, CLICK_EVENT> consumer);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> handleOpen(@Nullable final GameInventoryOpenConsumer<INVENTORY, PLAYER> consumer);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, CLICK_EVENT, CLOSE_EVENT> handleClose(@Nullable final GameInventoryCloseConsumer<CLOSE_EVENT> consumer);
}
