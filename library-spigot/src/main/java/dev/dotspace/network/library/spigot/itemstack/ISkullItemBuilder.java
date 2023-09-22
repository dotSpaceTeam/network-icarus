package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.network.library.game.itemstack.GameSkullItemBuilder;
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
 * Spigot implementation of {@link GameSkullItemBuilder}.
 */
public interface ISkullItemBuilder
    extends GameSkullItemBuilder<ItemStack, Material, Component, ItemFlag, Enchantment, Color, PlayerProfile> {
  /**
   * See {@link GameSkullItemBuilder#texture(GameSkin)}
   */
  @Override
  @NotNull ISkullItemBuilder texture(final @Nullable GameSkin<PlayerProfile> skin);
}
