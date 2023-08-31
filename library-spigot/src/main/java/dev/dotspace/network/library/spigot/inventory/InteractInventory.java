package dev.dotspace.network.library.spigot.inventory;

import dev.dotspace.network.library.game.inventory.AbstractGameInteractInventory;
import dev.dotspace.network.library.game.inventory.GameInteractInventory;
import dev.dotspace.network.library.game.inventory.GameInventoryClickConsumer;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class InteractInventory extends AbstractGameInteractInventory<Inventory, ItemStack, Player> {
  /**
   * Plugin instance for interface.
   */
  private final @NotNull AbstractPlugin plugin;

  protected InteractInventory(@NotNull AbstractPlugin plugin,
                              @NotNull Inventory itemStacks) {
    super(itemStacks);
    this.plugin = plugin;
  }

  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> setItem(@Nullable ItemStack itemStack,
                                                                              int slot) {
    //Todo: check if slot is used.

    //Check if slot is in range.
    this.checkInventorySlot(slot);

    this.inventory().setItem(slot, itemStack);
    return this;
  }

  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> removeItem(int slot) {
    this.inventory().setItem(slot, null);

    return this;
  }

  /**
   * See {@link GameInteractInventory#open(Object)}.
   */
  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> open(@Nullable Player player) {
    //Null check
    Objects.requireNonNull(player);

    //Open
    this.plugin.sync(() -> player.openInventory(this.inventory()));

    return this;
  }

  /**
   * See {@link GameInteractInventory#close(Object)}.
   */
  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> close(@Nullable Player player) {
    //Null check
    Objects.requireNonNull(player);

    //Check if inventory is this, if so close.
    if (player.getOpenInventory().getTopInventory() == this.inventory()) {
      this.plugin.sync(player::closeInventory);
    }

    return this;
  }

  /**
   * See {@link GameInteractInventory#closeAll()}.
   */
  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> closeAll() {
    this.plugin.sync(() -> {
      this.inventory()
          //All viewers
          .getViewers()
          .forEach(HumanEntity::closeInventory);
    });
    return this;
  }

  @SuppressWarnings("all")
  @Override
  public @NotNull <EVENT> GameInteractInventory<Inventory, ItemStack, Player> handleClick(int slot,
                                                                                          @Nullable GameInventoryClickConsumer<ItemStack, EVENT> consumer) {
    //Create new handle with predicate.
    this.handle(InventoryClickEvent.class, inventoryClickEvent -> {
      //Return if slot does not match.
      if (inventoryClickEvent.getSlot() != slot) {
        return;
      }
      //accept, errors will be passed to parent.
      ((GameInventoryClickConsumer) consumer).accept(inventoryClickEvent,
          inventoryClickEvent.getCurrentItem(),
          inventoryClickEvent.getSlot());
    });
    return this;
  }
}
