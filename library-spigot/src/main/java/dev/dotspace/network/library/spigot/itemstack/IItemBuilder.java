package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.network.library.game.itemstack.GameItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Spigot implementation of {@link GameItemBuilder}.
 */
public interface IItemBuilder
        extends GameItemBuilder<ItemStack, Material, Component, ItemFlag, Enchantment, Color, PlayerProfile> {
    /*
     * Nothing
     */
}
