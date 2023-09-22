package dev.dotspace.network.library.spigot.inventory;

import dev.dotspace.network.library.game.inventory.AbstractGameInteractInventory;
import dev.dotspace.network.library.game.inventory.GameInteractInventory;
import dev.dotspace.network.library.game.inventory.GameInventoryClickConsumer;
import dev.dotspace.network.library.game.inventory.GameInventoryEventConsumer;
import dev.dotspace.network.library.game.inventory.GameInventoryProvider;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class InteractInventory extends AbstractGameInteractInventory<Inventory, ItemStack, Player> {

  protected InteractInventory(@NotNull InventoryProvider provider,
                              @NotNull Inventory inventory) {
    super(provider, inventory);
  }

  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> setItem(@Nullable ItemStack itemStack,
                                                                              int slot) {
    //Pass to super class.
    super.setItem(itemStack, slot);

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
    this.provider().plugin().sync(() -> player.openInventory(this.inventory()));

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
      this.provider().plugin().sync(player::closeInventory);
    }

    return this;
  }

  /**
   * See {@link GameInteractInventory#closeAll()}.
   */
  @Override
  public @NotNull GameInteractInventory<Inventory, ItemStack, Player> closeAll() {
    this.provider().plugin().sync(() -> {
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
    super.handleClick(slot, consumer);

    //Create new handle with predicate.
    this.registerHandle(InventoryClickEvent.class, slot, inventoryClickEvent -> {
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

  @Override
  protected boolean itemInSlotPresent(int slot) {
    @Nullable final ItemStack itemStack = this.inventory().getItem(slot);
    //Return true if item is not null and not type of air.
    return itemStack != null && itemStack.getType() != Material.AIR;
  }

  /**
   * Handle event.
   */
  @Override
  protected <EVENT> void executeEvent(@Nullable EVENT event) {
    super.executeEvent(event);
  }

  @Override
  protected <EVENT> void registerHandle(@Nullable Class<EVENT> eventClass,
                                        int slot,
                                        @Nullable GameInventoryEventConsumer<EVENT> consumer) {
    super.registerHandle(eventClass, slot, consumer);
    this.provider().handleEvent(eventClass);
  }

  /**
   * Provider instance.
   */
  @Override
  public @NotNull InventoryProvider provider() {
    return (InventoryProvider) super.provider();
  }
}
