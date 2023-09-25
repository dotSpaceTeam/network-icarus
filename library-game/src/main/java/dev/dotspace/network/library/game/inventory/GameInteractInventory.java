package dev.dotspace.network.library.game.inventory;

import dev.dotspace.network.library.game.itemstack.GameItemEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR extends GameItemEditor<?, ?, ?, ?, ?, ?, ?>> {

  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> setItem(@Nullable final ITEM item,
                                                                                  final int slot,
                                                                                  @Nullable final GameInventoryClickConsumer<ITEM,
                                                                                      EVENT> consumer);

  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> setEditor(@Nullable final EDITOR editor,
                                                                                    final int slot,
                                                                                    @Nullable final GameInventoryClickConsumer<ITEM,
                                                                                        EVENT> consumer);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> setItem(@Nullable final ITEM item,
                                                                          final int slot);

  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> removeItem(final int slot);

  /**
   * Open inventory for player.
   *
   * @param player to open inventory for.
   * @return interface instance.
   * @throws NullPointerException if player is null.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> open(@Nullable final PLAYER player);

  /**
   * Close inventory for player.
   *
   * @param player to close inventory for.
   * @return interface instance.
   * @throws NullPointerException if player is null.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> close(@Nullable final PLAYER player);

  /**
   * Close inventory to all players.
   *
   * @return interface instance.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> closeAll();

  @NotNull INVENTORY inventory();

  /*
   * Handles
   */
  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> handleClick(final int slot,
                                                                                      @Nullable final GameInventoryClickConsumer<ITEM, EVENT> consumer);

  @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> handle(@Nullable final Class<EVENT> eventClass,
                                                                                 @Nullable final GameInventoryEventConsumer<EVENT> consumer);
}
