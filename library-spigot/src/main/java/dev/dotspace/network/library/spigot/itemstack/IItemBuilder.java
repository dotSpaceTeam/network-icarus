package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.network.library.game.itemstack.GameEnchantmentInfo;
import dev.dotspace.network.library.game.itemstack.GameItemBuilder;
import dev.dotspace.network.library.game.itemstack.GameLeatherArmorBuilder;
import dev.dotspace.network.library.game.itemstack.GameSkullItemBuilder;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.game.profile.GameSkin;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Spigot implementation of {@link GameItemBuilder}.
 */
public interface IItemBuilder
    extends GameItemBuilder<ItemStack, Material, Component, ItemFlag, Enchantment, Color, PlayerProfile> {
  /*
   * Nothing
   */

  /**
   * See {@link GameItemBuilder#amount(int)}
   */
  @Override
  @NotNull IItemBuilder amount(final int amount);

  /**
   * See {@link GameItemBuilder#name(Object)}
   */
  @Override
  @NotNull IItemBuilder name(final @Nullable Component component);

  /**
   * See {@link GameItemBuilder#name(Object)}
   */
  @Override
  @NotNull IItemBuilder name(final @Nullable MessageContext messageContext);

  /**
   * See {@link GameItemBuilder#lore(List)}
   */
  @Override
  @NotNull IItemBuilder lore(final @Nullable List<Component> lore);

  /**
   * See {@link GameItemBuilder#lore(MessageContext)}
   */
  @Override
  @NotNull IItemBuilder lore(final @Nullable MessageContext messageContext);

  /**
   * See {@link GameItemBuilder#appendLore(Object)}
   */
  @Override
  @NotNull IItemBuilder appendLore(final @Nullable Component component);

  /**
   * See {@link GameItemBuilder#flags(Object[])}
   */
  @Override
  @NotNull IItemBuilder flags(final @NotNull ItemFlag... itemFlags);

  /**
   * See {@link GameItemBuilder#flags(Set)}
   */
  @Override
  @NotNull IItemBuilder flags(final @Nullable Set<@NotNull ItemFlag> itemFlags);

  /**
   * See {@link GameItemBuilder#removeFlags(Object[])}
   */
  @Override
  @NotNull IItemBuilder removeFlags(final @NotNull ItemFlag... itemFlags);

  /**
   * Set item unbreakable.
   */
  @Override
  @NotNull IItemBuilder unbreakable(final boolean unbreakable);

  @NotNull IItemBuilder edit(@Nullable final ThrowableConsumer<ItemStack> consumer);

  /**
   * See {@link GameItemBuilder#enchantment(GameEnchantmentInfo)}
   */
  @Override
  @NotNull IItemBuilder enchantment(final @Nullable GameEnchantmentInfo<Enchantment> enchantmentInfo);

  /**
   * See {@link GameItemBuilder#removeEnchantment(GameEnchantmentInfo)}
   */
  @Override
  @NotNull IItemBuilder removeEnchantment(final @NotNull GameEnchantmentInfo<Enchantment> enchantment);

  /**
   * See {@link GameItemBuilder#leatherArmor(Object)}
   */
  @Override
  @NotNull ILeatherArmorBuilder leatherArmor(final @NotNull Color color);

  /**
   * See {@link GameItemBuilder#skull(GameSkin)}
   */
  @Override
  @NotNull ISkullItemBuilder skull(final @NotNull GameSkin<PlayerProfile> skin);

  /**
   * See {@link GameItemBuilder#buildAsync(Consumer)}
   */
  @Override
  @NotNull IItemBuilder buildAsync(final @Nullable Consumer<ItemStack> itemStackConsumer);
}
