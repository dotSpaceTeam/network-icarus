package dev.dotspace.network.library.game.inventory;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@Log4j2
public abstract class AbstractGameInventoryProvider<INVENTORY, ITEM, PLAYER>
    implements GameInventoryProvider<INVENTORY, ITEM, PLAYER> {

  /**
   * Map to store all inventories present.
   */
  private final List<GameInteractInventory<INVENTORY, ITEM, PLAYER>> providerInventoryList;

  public AbstractGameInventoryProvider() {
    this.providerInventoryList = new ArrayList<>();
  }

  /**
   * Handle inventory.
   *
   * @param inventory to be handled.
   */
  protected void handle(@NotNull final GameInteractInventory<INVENTORY, ITEM, PLAYER> inventory) {
    //Add inventory to handle list.
    this.providerInventoryList.add(inventory);
    log.info("Added inventory instance to be handled.");
  }

  /**
   * Ignore inventory from now on.
   *
   * @param inventory to be ignored.
   */
  protected void forget(@NotNull final GameInteractInventory<INVENTORY, ITEM, PLAYER> inventory) {
    //Remove inventory from handle list.
    this.providerInventoryList.remove(inventory);
    log.info("Removed inventory instance.");
  }

  /**
   * List of all present handled inventories.
   *
   * @return list of all inventories.
   */
  protected @NotNull List<GameInteractInventory<INVENTORY, ITEM, PLAYER>> inventoryList() {
    return new ArrayList<>(this.providerInventoryList);
  }
}
