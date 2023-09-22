package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.network.library.game.itemstack.GameEnchantmentInfo;
import dev.dotspace.network.library.game.itemstack.GameItemBuilder;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.game.profile.GameSkin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


//Todo
@Accessors(fluent = true)
public class ItemBuilder implements IItemBuilder {
  /**
   * Item to edit.
   */
  @Getter(AccessLevel.PROTECTED)
  private final ItemStack itemStack;

  private @Nullable MessageContext nameContext;
  private @Nullable MessageContext loreContext;

  public ItemBuilder(@Nullable final Material material) {
    this.itemStack = new ItemStack(material != null ? material : Material.AIR);
  }

  /**
   * See {@link IItemBuilder#amount(int)}.
   */
  @Override
  public @NotNull IItemBuilder amount(int amount) {
    this.itemStack.setAmount(amount);
    return this;
  }

  /**
   * See {@link IItemBuilder#name(Component)}.
   */
  @Override
  public @NotNull IItemBuilder name(@Nullable Component component) {
    //Set item name
    this.itemStack.editMeta(itemMeta -> itemMeta.displayName(component));
    return this;
  }

  /**
   * See {@link GameItemBuilder#name(Object)}
   */
  @Override
  public @NotNull IItemBuilder name(@Nullable MessageContext messageContext) {
    this.nameContext = messageContext;
    return this;
  }

  /**
   * See {@link IItemBuilder#lore(List)}
   */
  @Override
  public @NotNull IItemBuilder lore(@Nullable List<Component> lore) {
    this.itemStack.editMeta(itemMeta -> {
      //Set lore to null.
      itemMeta.lore(lore);
    });

    return this;
  }

  /**
   * See {@link IItemBuilder#lore(MessageContext)}
   */
  @Override
  public @NotNull IItemBuilder lore(@Nullable MessageContext messageContext) {
    //Set lore message context.
    this.loreContext = messageContext;
    return this;
  }

  /**
   * {@link IItemBuilder#appendLore(Component)}
   */
  @Override
  public @NotNull IItemBuilder appendLore(@Nullable Component component) {
    //Null check
    Objects.requireNonNull(component);

    this.itemStack.editMeta(itemMeta -> {
      @NotNull final List<Component> lore;
      //Create new lore
      if (itemMeta.hasLore()) {
        lore = Objects.requireNonNull(itemMeta.lore());
      } else {
        //Otherwise create new list.
        lore = new ArrayList<>();
      }

      //Append lore
      lore.add(component);

      //Set lore back to item
      itemMeta.lore(lore);
    });
    return this;
  }

  /**
   * See {@link IItemBuilder#flags(ItemFlag[])}
   */
  @Override
  public @NotNull IItemBuilder flags(@NotNull ItemFlag... itemFlags) {
    this.itemStack.addItemFlags(itemFlags);
    return this;
  }

  /**
   * See {@link IItemBuilder#flags(Set)}
   */
  @Override
  public @NotNull IItemBuilder flags(@Nullable Set<@NotNull ItemFlag> itemFlags) {
    //Null check
    Objects.requireNonNull(itemFlags);

    return this.flags(itemFlags.toArray(new ItemFlag[0]));
  }

  /**
   * See {@link IItemBuilder#removeFlags(ItemFlag...)}
   */
  @Override
  public @NotNull IItemBuilder removeFlags(@NotNull ItemFlag... itemFlags) {
    this.itemStack.removeItemFlags(itemFlags);
    return this;
  }

  /**
   * See {@link IItemBuilder#unbreakable(boolean)}
   */
  @Override
  public @NotNull IItemBuilder unbreakable(boolean unbreakable) {
    this.itemStack.editMeta(itemMeta -> itemMeta.setUnbreakable(unbreakable));
    return this;
  }

  @Override
  public @NotNull IItemBuilder edit(@Nullable ThrowableConsumer<ItemStack> consumer) {
    //Null check
    Objects.requireNonNull(consumer);

    try {
      consumer.accept(this.itemStack);
    } catch (final Throwable ignore) {
    }

    return this;
  }

  /**
   * See {@link IItemBuilder#enchantment(GameEnchantmentInfo)}
   */
  @Override
  public @NotNull IItemBuilder enchantment(@Nullable GameEnchantmentInfo<Enchantment> enchantmentInfo) {
    //Null check
    Objects.requireNonNull(enchantmentInfo);

    //If legal
    if (enchantmentInfo.legal()) {
      //Add legal enchantment.
      this.itemStack.addEnchantment(enchantmentInfo.type(), enchantmentInfo.level());
    } else {
      //Add illegal enchantment.
      this.itemStack.addUnsafeEnchantment(enchantmentInfo.type(), enchantmentInfo.level());
    }

    return this;
  }

  /**
   * See {@link IItemBuilder#removeEnchantment(GameEnchantmentInfo)}
   */
  @Override
  public @NotNull IItemBuilder removeEnchantment(@NotNull GameEnchantmentInfo<Enchantment> enchantment) {
    this.itemStack.removeEnchantment(enchantment.type());
    return this;
  }

  @Override
  public @NotNull ILeatherArmorBuilder leatherArmor(@NotNull Color color) {
    return null;
  }

  @Override
  public @NotNull ISkullItemBuilder skull(@NotNull GameSkin<PlayerProfile> skin) {
    return null;
  }

  @Override
  public @NotNull ItemStack build() {
    return null;
  }


  private final ExecutorService executorService = Executors.newCachedThreadPool();

  private final Component component = MiniMessage.miniMessage().deserialize("<gray>...");

  /**
   * See {@link IItemBuilder#buildAsync(Consumer)}
   */
  @Override
  public @NotNull IItemBuilder buildAsync(@Nullable Consumer<ItemStack> itemStackConsumer) {
    //Null check
    Objects.requireNonNull(itemStackConsumer);

    executorService.execute(() -> {
      if (this.nameContext != null) {
        this.name(this.component);
        //Update item to set loading name.
        itemStackConsumer.accept(this.itemStack);

        this.name(MiniMessage.miniMessage().deserialize(this.nameContext.complete()));
        //Updated item name.
        itemStackConsumer.accept(this.itemStack);
      }
    });

    return this;
  }
}
