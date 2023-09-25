package dev.dotspace.network.library.spigot.inventory;

import dev.dotspace.network.library.game.inventory.AbstractGameInteractInventory;
import dev.dotspace.network.library.game.inventory.GameInteractInventory;
import dev.dotspace.network.library.game.inventory.GameInventoryClickConsumer;
import dev.dotspace.network.library.game.inventory.GameInventoryEventConsumer;
import dev.dotspace.network.library.game.inventory.InventoryConsistency;
import dev.dotspace.network.library.spigot.itemstack.IItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class InteractInventory
    extends AbstractGameInteractInventory<Inventory, InventoryType, ItemStack, Player, Component, IItemEditor>
    implements IInteractInventory {

  protected InteractInventory(@NotNull IInventoryProvider provider,
                              @NotNull Inventory inventory) {
    super(provider, inventory);

    //Consume inventory close.
    this.handle(InventoryCloseEvent.class, event -> {
      //Ignore if consistency is persistent
      if (this.consistency() == InventoryConsistency.PERSISTENT) {
        return;
      }

      //Return if viewers are bigger than 1
      if (event.getInventory().getViewers().size()>1) {
        return;
      }

      //Unregister inventory.
      this.provider().unregister(this);
    });
  }


  @Override
  public @NotNull <EVENT> IInteractInventory setEditor(@Nullable IItemEditor editor,
                                                       int slot,
                                                       @Nullable GameInventoryClickConsumer<ItemStack, EVENT> consumer) {
    //Null check
    Objects.requireNonNull(editor);
    Objects.requireNonNull(consumer);

    //Run editor.
    editor.handle(itemStack -> {
      //Set item in inventory.
      this.setItem(itemStack, slot);
    }).complete() /* Set item */;

    //Pass click handle.
    this.handleClick(slot, consumer);

    return this;
  }

  @Override
  public @NotNull IInteractInventory setItem(@Nullable ItemStack itemStack,
                                             int slot) {
    //Pass to super class.
    super.setItem(itemStack, slot);

    this.inventory().setItem(slot, itemStack);
    return this;
  }

  @Override
  public @NotNull IInteractInventory removeItem(int slot) {
    //Remove item.
    this.inventory().setItem(slot, null);
    //Remove handle.
    this.removeHandleFromSlot(slot);
    return this;
  }

  /**
   * See {@link GameInteractInventory#open(Object)}.
   */
  @Override
  public @NotNull IInteractInventory open(@Nullable Player player) {
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
  public @NotNull IInteractInventory close(@Nullable Player player) {
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
  public @NotNull IInteractInventory closeAll() {
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
  public @NotNull <EVENT> IInteractInventory handleClick(int slot,
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
