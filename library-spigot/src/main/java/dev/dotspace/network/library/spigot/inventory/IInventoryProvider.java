package dev.dotspace.network.library.spigot.inventory;

import dev.dotspace.network.library.game.inventory.GameInventoryProvider;
import dev.dotspace.network.library.spigot.itemstack.IItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public interface IInventoryProvider
    extends GameInventoryProvider<Inventory, InventoryType, ItemStack, Player, Component, IItemEditor> {
}
