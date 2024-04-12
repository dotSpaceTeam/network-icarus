package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.network.library.game.itemstack.GameLeatherArmorEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Spigot implementation of {@link GameLeatherArmorEditor}.
 */
public interface ILeatherArmorEditor
        extends GameLeatherArmorEditor<ItemStack, Material, Component, ItemFlag, Enchantment, Color, PlayerProfile> {
  /**
   * See {@link GameLeatherArmorEditor#color(Object)}
   */
  @Override
  @NotNull ILeatherArmorEditor color(final @Nullable Color color);
}
