package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.network.library.game.profile.GameSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public interface GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {

    /**
     * Get material of builder
     *
     * @return Material of itemStack to build
     */
    @NotNull MATERIAL material();

    /**
     * Get amount
     *
     * @return amount of item
     */
    @NotNull OptionalInt amount();

    /**
     * Set amount of stack for itemStack
     *
     * @param amount value between 1 and 64
     * @return class instance
     */
    //Todo: add check
    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> amount(final int amount);

    /**
     * Currently set name
     *
     * @return current nam
     */
    @NotNull Optional<TEXT> name();

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> name(@Nullable final TEXT component);

    @NotNull Optional<@NotNull List<TEXT>> lore();

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> lore(@Nullable final List<TEXT> lore);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> appendLore(@Nullable final TEXT component);

    @NotNull Optional<@NotNull Set<ITEM_FLAG>> flags();

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> flags(@NotNull final ITEM_FLAG... itemFlags);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> flags(@Nullable final Set<@NotNull ITEM_FLAG> itemFlags);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> removeFlags(@NotNull final ITEM_FLAG... itemFlags);

    boolean unbreakable();

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> unbreakable(final boolean unbreakable);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> enchantment(@NotNull final GameEnchantmentInfo<ENCHANTMENT> enchantmentInfo);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> enchantments(@Nullable final Set<@NotNull GameEnchantmentInfo<ENCHANTMENT>> enchantmentInfo);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> removeEnchantment(@NotNull final GameEnchantmentInfo<ENCHANTMENT> enchantment);

    @NotNull GameLeatherArmorBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> leatherArmor(@NotNull final COLOR color);

    @NotNull GameSkullItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> skull(@NotNull final GameSkin<PROFILE> skin);

    @NotNull GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> deepClone();

    /**
     * Get item.
     *
     * @return item that was configured.
     */
    @NotNull ITEM item();
}