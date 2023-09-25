package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.network.library.game.itemstack.GameSkullItemEditor;
import dev.dotspace.network.library.game.profile.GameSkin;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Spigot implementation of {@link GameSkullItemEditor}.
 */
public interface ISkullItemEditor
    extends GameSkullItemEditor<ItemStack, Material, Component, ItemFlag, Enchantment, Color, PlayerProfile> {
  /**
   * See {@link GameSkullItemEditor#texture(GameSkin)}
   */
  @Override
  @NotNull ISkullItemEditor texture(final @Nullable GameSkin<PlayerProfile> skin);
}
