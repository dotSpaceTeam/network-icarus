package dev.dotspace.network.library.spigot.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class tre {

  {
    new InteractInventory(null, Bukkit.createInventory(null, 9))
        .handle(InventoryClickEvent.class, inventoryClickEvent -> {

        })
        .handleClick(5, (o, itemStack, slot) -> {

        })
        .setItem(new ItemStack(Material.STONE), 5, (o, itemStack, slot) -> {

        });
  }

}
