package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.game.profile.GameSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;


public interface GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
  /**
   * Set amount of stack for itemStack
   *
   * @param amount value between 1 and 64
   * @return class instance
   */
  //Todo: add check
  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> amount(final int amount);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> name(@Nullable final TEXT component);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> name(@Nullable final MessageContext messageContext);

  /**
   * Set null to remove lore.
   *
   * @param lore
   * @return
   */
  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> lore(@Nullable final List<TEXT> lore);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> lore(@Nullable final MessageContext messageContext);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> appendLore(@Nullable final TEXT component);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> flags(@NotNull final ITEM_FLAG... itemFlags);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> flags(@Nullable final Set<@NotNull ITEM_FLAG> itemFlags);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> removeFlags(@NotNull final ITEM_FLAG... itemFlags);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> unbreakable(final boolean unbreakable);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> enchantment(@Nullable final GameEnchantmentInfo<ENCHANTMENT> enchantmentInfo);

  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> removeEnchantment(@Nullable final GameEnchantmentInfo<ENCHANTMENT> enchantment);

  @NotNull
  GameLeatherArmorEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> leatherArmor(@Nullable final COLOR color);

  @NotNull
  GameSkullItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> skull(@Nullable final GameSkin<PROFILE> skin);

  @NotNull GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> edit(@Nullable final ThrowableConsumer<ITEM> consumer);

  /**
   * Get item.
   *
   * @return item that was configured.
   */
  @NotNull Response<ITEM> complete();

  /**
   * Handle state of item. Also update every stage of complete method.
   *
   * @param itemConsumer every update of text will be executed here.
   * @return item that was configured.
   */
  @NotNull
  GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> handle(@Nullable final ThrowableConsumer<ITEM> handleConsumer);
}
