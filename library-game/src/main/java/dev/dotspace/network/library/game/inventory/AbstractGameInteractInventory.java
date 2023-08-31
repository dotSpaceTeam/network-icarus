package dev.dotspace.network.library.game.inventory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Log4j2
@Accessors(fluent=true)
public abstract class AbstractGameInteractInventory<INVENTORY, ITEM, PLAYER>
    implements GameInteractInventory<INVENTORY, ITEM, PLAYER> {

  @Getter
  private final @NotNull INVENTORY inventory;
  private final @NotNull Multimap<Class<?>, GameInventoryEventConsumer<?>> eventConsumerMultimap;

  protected AbstractGameInteractInventory(@NotNull final INVENTORY inventory) {
    this.inventory = inventory;
    this.eventConsumerMultimap = HashMultimap.create();
  }

  @Override
  public @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER> setItem(@Nullable ITEM item,
                                                                                 int slot,
                                                                                 @Nullable GameInventoryClickConsumer<ITEM, EVENT> consumer) {
    this.setItem(item, slot);
    this.handleClick(slot, consumer);
    return this;
  }

  @Override
  public @NotNull <EVENT> GameInteractInventory<INVENTORY, ITEM, PLAYER> handle(@Nullable Class<EVENT> eventClass,
                                                                                @Nullable GameInventoryEventConsumer<EVENT> consumer) {
    this.eventConsumerMultimap.put(eventClass, consumer);
    return this;
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
    this.eventConsumerMultimap
        //Get all handles of class
        .get(event.getClass())
        //Loop trough
        .forEach(gameInventoryEventConsumer -> {
          try {
            //Accept event.
            ((GameInventoryEventConsumer<EVENT>) gameInventoryEventConsumer).accept(event);
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


}
