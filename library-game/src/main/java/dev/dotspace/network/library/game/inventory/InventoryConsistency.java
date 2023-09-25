package dev.dotspace.network.library.game.inventory;

/**
 * Consistency of inventory.
 * <p>
 * Default -> VOLATILE.
 */
public enum InventoryConsistency {
  /**
   * Inventory should be deleted and unhandled after last close.
   */
  VOLATILE,

  /**
   * Inventory is stored in variable and will be used multiple times.
   */
  PERSISTENT;
}
