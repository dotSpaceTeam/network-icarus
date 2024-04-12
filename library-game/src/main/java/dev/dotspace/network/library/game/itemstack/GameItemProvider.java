package dev.dotspace.network.library.game.itemstack;

import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface GameItemProvider<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> extends Provider {
  /**
   * Create editor from material.
   *
   * @param material to create editor.
   * @return editor instance.
   */
  @NotNull GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> material(@Nullable final MATERIAL material);

  /**
   * Create editor from existing item.
   *
   * @param item to create editor.
   * @return editor instance.
   */
  @NotNull GameItemEditor<ITEM, MATERIAL, TEXT, ITEM_FLAG, ENCHANTMENT, COLOR, PROFILE> edit(@Nullable final ITEM item);
}
