package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.network.library.game.profile.GameSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface GameSkullItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE>
        extends GameItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
    @NotNull GameSkullItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> texture(@Nullable final GameSkin<PROFILE> skin);
}
