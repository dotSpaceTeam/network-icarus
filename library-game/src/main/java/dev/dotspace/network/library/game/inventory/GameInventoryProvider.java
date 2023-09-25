package dev.dotspace.network.library.game.inventory;

import dev.dotspace.network.library.game.itemstack.GameItemEditor;
import dev.dotspace.network.library.game.message.context.IMessageContext;
import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameInventoryProvider
    <INVENTORY, TYPE, ITEM, PLAYER, TEXT, EDITOR extends GameItemEditor<?, ?, ?, ?, ?, ?, ?>> extends Provider {
  /**
   * Create inventory with game inventory.
   *
   * @param inventory to create icarus inventory from.
   * @return created instance of {@link GameInteractInventory}.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> inventory(@Nullable final INVENTORY inventory);

  /**
   * Create inventory with name and size.
   *
   * @param name of inventory.
   * @param size of inventory.
   * @return created instance of {@link GameInteractInventory}.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> inventory(@Nullable final TEXT name,
                                                                            final int size);

  /**
   * Create inventory with name and size.
   *
   * @param messageContext to get name of inventory.
   * @param size           of inventory.
   * @return created instance of {@link GameInteractInventory}.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> inventory(@Nullable final IMessageContext messageContext,
                                                                            final int size);
}
