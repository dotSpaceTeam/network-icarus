package dev.dotspace.network.library.game.itemstack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface GameLeatherArmorBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE>
        extends GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
    /**
     * Specifies the color to be used for the builder
     *
     * @return color as COLOR
     */
    @NotNull Optional<COLOR> color();

    /**
     * Sets the color to be transferred to the armor
     *
     * @param color update builder color with a {@link COLOR}
     */
    @NotNull GameLeatherArmorBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> color(@Nullable final COLOR color);
}
