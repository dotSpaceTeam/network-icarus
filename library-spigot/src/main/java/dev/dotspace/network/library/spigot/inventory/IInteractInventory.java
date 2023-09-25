package dev.dotspace.network.library.spigot.inventory;

import dev.dotspace.network.library.game.inventory.GameInteractInventory;
import dev.dotspace.network.library.spigot.itemstack.IItemEditor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public interface IInteractInventory extends GameInteractInventory<Inventory, ItemStack, Player, IItemEditor> {
}
