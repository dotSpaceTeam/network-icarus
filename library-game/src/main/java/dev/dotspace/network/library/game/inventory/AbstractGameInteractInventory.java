package dev.dotspace.network.library.game.inventory;

import dev.dotspace.network.library.game.itemstack.GameItemEditor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Log4j2
@Accessors(fluent=true)
public abstract class AbstractGameInteractInventory<INVENTORY, TYPE, ITEM, PLAYER, TEXT, EDITOR extends GameItemEditor<?, ?, ?, ?, ?, ?, ?>>
    implements GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> {
  @Getter
  private final @NotNull GameInventoryProvider<INVENTORY, TYPE, ITEM, PLAYER, TEXT, EDITOR> provider;
  @Getter
  private final @NotNull INVENTORY inventory;
  private final @NotNull List<EventConsumer> eventConsumerList;
  @Getter
  private @NotNull InventoryConsistency consistency = InventoryConsistency.VOLATILE;

  protected AbstractGameInteractInventory(@NotNull GameInventoryProvider<INVENTORY, TYPE, ITEM, PLAYER, TEXT, EDITOR> provider,
                                          @NotNull final INVENTORY inventory) {
    this.provider = provider;
    this.inventory = inventory;
    this.eventConsumerList = new ArrayList<>();
  }

  @Override
  public @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> setItem(@Nullable ITEM item,
                                                                                 int slot) {
    //Remove handles.
    this.removeHandleFromSlot(slot);

    //Check if slot is in range.
    this.checkInventorySlot(slot);

    return this;
  }

  @Override
  public @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> setItem(@Nullable ITEM item,
                                                                                         int slot,
                                                                                         @Nullable GameInventoryClickConsumer<ITEM, EVENT> consumer) {
    this.setItem(item, slot);
    this.handleClick(slot, consumer);
    return this;
  }

  @Override
  public @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> handle(@Nullable Class<EVENT> eventClass,
                                                                                        @Nullable GameInventoryEventConsumer<EVENT> consumer) {
    this.registerHandle(eventClass, -1, consumer);
    return this;
  }

  @Override
  public @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> handleClick(int slot,
                                                                                             @Nullable GameInventoryClickConsumer<ITEM, EVENT> consumer) {
    //Null check
    if (!this.itemInSlotPresent(slot)) {
      return null;
    }

    return this;
  }

  @Override
  public @NotNull GameInteractInventory<INVENTORY, ITEM, PLAYER, EDITOR> consistency(@Nullable InventoryConsistency consistency) {
    //Null check
    Objects.requireNonNull(consistency);

    //Set new consistency
    this.consistency = consistency;

    return this;
  }

  protected void removeHandleFromSlot(final int slot) {
    this.eventConsumerList
        .removeIf(eventConsumer -> eventConsumer.slot == slot);
  }

  protected <EVENT> void registerHandle(@Nullable Class<EVENT> eventClass,
                                        final int slot,
                                        @Nullable GameInventoryEventConsumer<EVENT> consumer) {
    //Null check
    Objects.requireNonNull(eventClass);
    Objects.requireNonNull(consumer);

    this.eventConsumerList.add(new EventConsumer(eventClass, slot, consumer));
  }

  /**
   * Execute event.
   *
   * @param event   instance to execute, if null call will be ignored.
   * @param <EVENT> generic type of event.
   */
  @SuppressWarnings("unchecked")
  protected <EVENT> void executeEvent(@Nullable final EVENT event) {
    //Return if event is null - nothing to execute.
    if (event == null) {
      return;
    }
    this.eventConsumerList
        .stream()
        //Get all handles of class
        .filter(eventConsumer -> eventConsumer.eventClass() == event.getClass())
        //Loop trough
        .forEach(gameInventoryEventConsumer -> {
          try {
            //Accept event.
            ((GameInventoryEventConsumer<EVENT>) gameInventoryEventConsumer.consumer()).accept(event);
          } catch (final Throwable error) {
            //Error on consumer throw or class can't be cast.
            log.warn("Error while executing inventory handle.");
          }
        });
  }

  /**
   * Check if slot is present in inventory.
   *
   * @param slot to check.
   * @throws IllegalArgumentException if slot is out of range.
   */
  protected void checkInventorySlot(final int slot) throws IllegalArgumentException {
    //Return if slot is possible
    if (slot>=0 && slot<54) {
      return;
    }
    throw new IllegalArgumentException("Slot out of range!");
  }

  protected abstract boolean itemInSlotPresent(final int slot);

  private record EventConsumer(@NotNull Class<?> eventClass,
                               int slot,
                               @NotNull GameInventoryEventConsumer<?> consumer
  ) {

  }
}
