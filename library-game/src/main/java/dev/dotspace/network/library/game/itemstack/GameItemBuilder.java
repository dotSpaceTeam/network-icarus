package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.game.profile.GameSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


public interface GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
  /**
   * Set amount of stack for itemStack
   *
   * @param amount value between 1 and 64
   * @return class instance
   */
  //Todo: add check
  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> amount(final int amount);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> name(@Nullable final TEXT component);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> name(@Nullable final MessageContext messageContext);

  /**
   * Set null to remove lore.
   *
   * @param lore
   * @return
   */
  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> lore(@Nullable final List<TEXT> lore);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> lore(@Nullable final MessageContext messageContext);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> appendLore(@Nullable final TEXT component);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> flags(@NotNull final ITEM_FLAG... itemFlags);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> flags(@Nullable final Set<@NotNull ITEM_FLAG> itemFlags);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> removeFlags(@NotNull final ITEM_FLAG... itemFlags);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> unbreakable(final boolean unbreakable);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> enchantment(@Nullable final GameEnchantmentInfo<ENCHANTMENT> enchantmentInfo);

  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> removeEnchantment(@NotNull final GameEnchantmentInfo<ENCHANTMENT> enchantment);

  @NotNull GameLeatherArmorBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> leatherArmor(@NotNull final COLOR color);

  @NotNull GameSkullItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> skull(@NotNull final GameSkin<PROFILE> skin);

  /**
   * Get item.
   *
   * @return item that was configured.
   */
  @NotNull ITEM build();

  /**
   * Get item.
   *
   * @param itemConsumer every update of text will be executed here.
   * @return item that was configured.
   */
  @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> buildAsync(@Nullable final Consumer<ITEM> itemConsumer);
}
