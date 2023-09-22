package dev.dotspace.network.library.spigot.inventory;

import dev.dotspace.network.library.game.inventory.GameInventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public interface IInventoryProvider extends GameInventoryProvider<Inventory, ItemStack, Player> {
}
