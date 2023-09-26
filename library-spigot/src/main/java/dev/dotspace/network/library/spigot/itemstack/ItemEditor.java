package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.game.itemstack.GameEnchantmentInfo;
import dev.dotspace.network.library.game.itemstack.GameItemEditor;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.game.profile.GameSkin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
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

import static dev.dotspace.network.library.game.message.ComponentUtils.*;


//Todo
@Log4j2
@Accessors(fluent=true)
public class ItemEditor implements IItemEditor {
  /**
   * Item to edit.
   */
  @Getter(AccessLevel.PROTECTED)
  private final ItemStack itemStack;

  private final @NotNull List<ThrowableConsumer<ItemStack>> handleList;

  private @Nullable MessageContext nameContext;
  private @Nullable MessageContext loreContext;


  protected ItemEditor(@Nullable final Material material) {
    this(new ItemStack(material != null ? material : Material.AIR));
  }

  protected ItemEditor(@NotNull final ItemStack itemStack) {
    this.itemStack = itemStack;
    this.handleList = new ArrayList<>();
  }

  /**
   * See {@link IItemEditor#amount(int)}.
   */
  @Override
  public @NotNull IItemEditor amount(int amount) {
    this.itemStack.setAmount(amount);
    return this;
  }

  /**
   * See {@link GameItemEditor#name(Object)}
   */
  @Override
  public @NotNull IItemEditor name(@Nullable Component component) {
    //Set item name
    this.itemStack.editMeta(itemMeta -> itemMeta.displayName(component));
    return this;
  }

  /**
   * See {@link GameItemEditor#name(Object)}
   */
  @Override
  public @NotNull IItemEditor name(@Nullable MessageContext messageContext) {
    this.nameContext = messageContext;
    return this;
  }

  /**
   * See {@link IItemEditor#lore(List)}
   */
  @Override
  public @NotNull IItemEditor lore(@Nullable List<Component> lore) {
    this.itemStack.editMeta(itemMeta -> {
      //Set lore to null.
      itemMeta.lore(lore);
    });

    return this;
  }

  /**
   * See {@link IItemEditor#lore(MessageContext)}
   */
  @Override
  public @NotNull IItemEditor lore(@Nullable MessageContext messageContext) {
    //Set lore message context.
    this.loreContext = messageContext;
    return this;
  }

  /**
   * {@link GameItemEditor#appendLore(Object)}
   */
  @Override
  public @NotNull IItemEditor appendLore(@Nullable Component component) {
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
   * See {@link GameItemEditor#flags(Object[])}
   */
  @Override
  public @NotNull IItemEditor flags(@NotNull ItemFlag... itemFlags) {
    this.itemStack.addItemFlags(itemFlags);
    return this;
  }

  /**
   * See {@link IItemEditor#flags(Set)}
   */
  @Override
  public @NotNull IItemEditor flags(@Nullable Set<@NotNull ItemFlag> itemFlags) {
    //Null check
    Objects.requireNonNull(itemFlags);

    return this.flags(itemFlags.toArray(new ItemFlag[0]));
  }

  /**
   * See {@link GameItemEditor#removeFlags(Object...)}
   */
  @Override
  public @NotNull IItemEditor removeFlags(@NotNull ItemFlag... itemFlags) {
    this.itemStack.removeItemFlags(itemFlags);
    return this;
  }

  /**
   * See {@link GameItemEditor#unbreakable(boolean)}
   */
  @Override
  public @NotNull IItemEditor unbreakable(boolean unbreakable) {
    this.itemStack.editMeta(itemMeta -> itemMeta.setUnbreakable(unbreakable));
    return this;
  }

  /**
   * See {@link GameItemEditor#edit(ThrowableConsumer)}
   */
  @Override
  public @NotNull IItemEditor edit(@Nullable ThrowableConsumer<ItemStack> consumer) {
    //Null check
    Objects.requireNonNull(consumer);

    try {
      consumer.accept(this.itemStack);
    } catch (final Throwable ignore) {
    }
    return this;
  }

  /**
   * See {@link IItemEditor#enchantment(GameEnchantmentInfo)}
   */
  @Override
  public @NotNull IItemEditor enchantment(@Nullable GameEnchantmentInfo<Enchantment> enchantmentInfo) {
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
   * See {@link IItemEditor#removeEnchantment(GameEnchantmentInfo)}
   */
  @Override
  public @NotNull IItemEditor removeEnchantment(@Nullable GameEnchantmentInfo<Enchantment> enchantment) {
    //Null check
    Objects.requireNonNull(enchantment);

    this.itemStack.removeEnchantment(enchantment.type());
    return this;
  }

  /**
   * See {@link GameItemEditor#leatherArmor(Object)}
   */
  @Override
  public @NotNull ILeatherArmorEditor leatherArmor(@Nullable Color color) {
    return new LeatherArmorEditor(this.itemStack).color(color);
  }

  @Override
  public @NotNull ISkullItemEditor skull(@Nullable GameSkin<PlayerProfile> skin) {
    return new SkullItemEditor(this.itemStack).skull(skin);
  }

  @Override
  public @NotNull ItemStack forceComplete() {
    //Set name.
    if (this.nameContext != null) {
      this.name(waitingComponent());
      //Update item to set loading name.
      this.executeHandle();

      //Set name.
      this.name(component(this.nameContext.forceComplete()));
    }

    //Set lore.
    if (this.loreContext != null) {
      //Single list.
      this.lore(waitingComponentList());
      //Update item to set loading name.
      this.executeHandle();

      //Set lore
      this.lore((List<Component>) componentList(loreContext.forceComplete()));
    }

    //Updated item name.
    this.executeHandle();

    //Return local stack.
    return this.itemStack;
  }

  @Override
  public @NotNull Response<ItemStack> complete() {
    return Library.responseService().response(this::forceComplete);
  }

  @Override
  public @NotNull IItemEditor handle(@Nullable ThrowableConsumer<ItemStack> handleConsumer) {
    //Null check
    Objects.requireNonNull(handleConsumer);

    //Add event to handle.
    this.handleList.add(handleConsumer);
    return this;
  }

  /**
   * Handle builder handle.
   */
  private void executeHandle() {
    //Loop trough every consumer
    for (final ThrowableConsumer<ItemStack> consumer : this.handleList) {
      try {
        //Handle item.
        consumer.accept(this.itemStack);
      } catch (final Throwable throwable) {
        //Error
        log.warn("Error while handling itemstack.", throwable);
      }
    }
  }
}
