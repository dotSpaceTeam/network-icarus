package dev.dotspace.network.library.game.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameInteractInventory<INVENTORY, ITEM, PLAYER> {

  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER> setItem(@Nullable final ITEM item,
                                                                          final int slot,
                                                                          @Nullable final GameInventoryClickConsumer<ITEM,
                                                                              EVENT> consumer);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER> setItem(@Nullable final ITEM item,
                                                                  final int slot);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER> removeItem(final int slot);

  /**
   * Open inventory for player.
   *
   * @param player to open inventory for.
   * @return interface instance.
   * @throws NullPointerException if player is null.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER> open(@Nullable final PLAYER player);

  /**
   * Close inventory for player.
   *
   * @param player to close inventory for.
   * @return interface instance.
   * @throws NullPointerException if player is null.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER> close(@Nullable final PLAYER player);

  /**
   * Close inventory to all players.
   *
   * @return interface instance.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER> closeAll();

  @NotNull INVENTORY inventory();

  /*
   * Handles
   */
  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER> handleClick(final int slot,
                                                                              @Nullable final GameInventoryClickConsumer<ITEM, EVENT> consumer);

  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER> handle(@Nullable final Class<EVENT> eventClass,
                                                                         @Nullable final GameInventoryEventConsumer<EVENT> consumer);
}
