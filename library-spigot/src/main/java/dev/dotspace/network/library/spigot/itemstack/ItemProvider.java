package dev.dotspace.network.library.spigot.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public final class ItemProvider implements IItemProvider {
  @Override
  public @NotNull IItemEditor material(@Nullable Material material) {
    //Null check
    Objects.requireNonNull(material);


    return new ItemEditor(material);
  }

  @Override
  public @NotNull IItemEditor edit(@Nullable ItemStack itemStack) {
    //Null check
    Objects.requireNonNull(itemStack);

    return new ItemEditor(itemStack);
  }
}
