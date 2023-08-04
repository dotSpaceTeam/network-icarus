package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.network.library.game.itemstack.ILeatherArmorBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Spigot implementation of {@link ILeatherArmorBuilder}.
 */
public interface ISpigotLeatherArmorBuilder
        extends ILeatherArmorBuilder<ItemStack, Material, Component, ItemFlag, Enchantment, Color, PlayerProfile> {
    /*
     * Nothing
     */
}
