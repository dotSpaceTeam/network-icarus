package dev.dotspace.network.library.game.itemstack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameLeatherArmorEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE>
        extends GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
    /**
     * Sets the color to be transferred to the armor
     *
     * @param color update builder color with a {@link COLOR}
     */
    @NotNull GameLeatherArmorEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> color(@Nullable final COLOR color);
}
