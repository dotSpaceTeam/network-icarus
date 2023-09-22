package dev.dotspace.network.library.game.inventory;

import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameInventoryProvider<INVENTORY, ITEM, PLAYER> extends Provider {
  /**
   * Create inventory with game inventory.
   *
   * @param inventory to create icarus inventory from.
   * @return created instance of {@link GameInteractInventory}.
   */
  @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER> inventory(@Nullable final INVENTORY inventory);
}
