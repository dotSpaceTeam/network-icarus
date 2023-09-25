package dev.dotspace.network.library.spigot.inventory;

import com.google.inject.Inject;
import dev.dotspace.network.library.game.inventory.AbstractGameInventoryProvider;
import dev.dotspace.network.library.game.message.context.IMessageContext;
import dev.dotspace.network.library.spigot.itemstack.IItemEditor;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Log4j2
@Accessors(fluent=true)
public final class InventoryProvider
    extends AbstractGameInventoryProvider<Inventory, InventoryType, ItemStack, Player, Component, IItemEditor>
    implements IInventoryProvider, Listener {

  @Getter(AccessLevel.PROTECTED)
  private final AbstractPlugin plugin;
  /**
   * List of all handled events.
   */
  private final List<Class<? extends Event>> handledEventList;

  @Inject
  public InventoryProvider(AbstractPlugin plugin) {
    this.plugin = plugin;
    this.handledEventList = new ArrayList<>();

    //Register listener
  }

  @Override
  public @NotNull IInteractInventory inventory(@Nullable Inventory inventory) {
    //Null check
    Objects.requireNonNull(inventory);

    //Create instance.
    final InteractInventory interactInventory
        = new InteractInventory(this, Bukkit.createInventory(null, 9));

    //Handle inventory
    this.handle(interactInventory);

    log.info("Registered inventory.");

    return interactInventory;
  }

  @Override
  public @NotNull IInteractInventory inventory(@Nullable IMessageContext messageContext,
                                               int size) {
    //Null check
    Objects.requireNonNull(messageContext);

    return null;
  }

  /**
   * See {@link dev.dotspace.network.library.game.inventory.GameInventoryProvider#inventory(Object, int)}
   */
  @Override
  public @NotNull IInteractInventory inventory(@Nullable Component name,
                                               int size) {
    //Null check
    Objects.requireNonNull(name);

    //Call implemented method.
    return this.inventory(Bukkit.createInventory(null, size, name));
  }

  /**
   * Unregister interface
   */
  void unregister(@NotNull final InteractInventory interactInventory) {
    this.forget(interactInventory);
    log.info("Unregister inventory.");
  }

  /**
   * Register new event instance.
   *
   * @param eventClass class
   */
  @SuppressWarnings("unchecked")
  void handleEvent(@Nullable final Class<?> eventClass) {
    //Null check
    Objects.requireNonNull(eventClass);

    final Class<? extends Event> inventoryEvent;

    try {
      inventoryEvent = (Class<? extends Event>) eventClass;
    } catch (final Exception exception) {
      log.warn("Error while binding event={} to inventory provider.", eventClass.getSimpleName());
      return;
    }

    //Return if event is already handled.
    if (this.handledEventList.contains(inventoryEvent)) {
      return;
    }

    //Add event as handled.
    this.handledEventList.add(inventoryEvent);

    //Get plugin for provider
    this.plugin()
        //Server plugin manager
        .getServer()
        .getPluginManager()
        //Register inventory event
        .registerEvent(inventoryEvent, this, EventPriority.NORMAL, (listener, event) -> {
          //Get inventory for event
          final Inventory inventory = ((InventoryEvent) event).getInventory();
          //Loop trough every inventory.
          this.inventoryList().forEach(interactInventory -> {
            //Return if inventory is not to interact inventory.
            if (interactInventory.inventory() != inventory) {
              return; //Next inventory
            }
            //Pass event to handle.
            ((InteractInventory) interactInventory).executeEvent(event);
          });

        }, this.plugin);

    log.info("Activated event={} for inventory provider.", eventClass.getSimpleName());
  }
}
