package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.network.library.game.profile.ISkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ISkullItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE>
        extends IItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> {
    @NotNull Optional<ISkin<PROFILE>> texture();

    @NotNull ISkullItemBuilder<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> texture(@Nullable final ISkin<PROFILE> skin);
}
