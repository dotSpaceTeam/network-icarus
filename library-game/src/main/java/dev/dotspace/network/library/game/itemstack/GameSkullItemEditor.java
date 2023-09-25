package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.network.library.game.profile.GameSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameSkullItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE>
        extends GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
    @NotNull GameSkullItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> texture(@Nullable final GameSkin<PROFILE> skin);
}
