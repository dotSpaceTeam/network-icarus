package dev.dotspace.network.library.spigot.itemstack;

import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


@Log4j2
@Accessors(fluent=true)
public class LeatherArmorEditor extends ItemEditor implements ILeatherArmorEditor {
  /**
   * Pass to {@link ItemEditor}
   */
  protected LeatherArmorEditor(@NotNull ItemStack itemStack) {
    super(itemStack);
  }

  @Override
  public @NotNull ILeatherArmorEditor color(@Nullable Color color) {
    //Null check
    Objects.requireNonNull(color);

    //Throw if item is not a leatherArmor
    if (!(this.itemStack().getItemMeta() instanceof LeatherArmorMeta leatherArmorMeta)) {
      log.warn("Given item is not a leather item.");
      return this;
    }

    //Set color
    leatherArmorMeta.setColor(color);

    return this;
  }
}
