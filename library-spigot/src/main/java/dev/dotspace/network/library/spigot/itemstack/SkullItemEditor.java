package dev.dotspace.network.library.spigot.itemstack;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.dotspace.network.library.game.profile.GameSkin;
import dev.dotspace.network.library.game.profile.SkinOrigin;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


@Log4j2
public class SkullItemEditor extends ItemEditor implements ISkullItemEditor {
  /**
   * Pass to {@link ItemEditor}
   */
  protected SkullItemEditor(@NotNull ItemStack itemStack) {
    super(itemStack);
  }

  @Override
  public @NotNull ISkullItemEditor texture(@Nullable GameSkin<PlayerProfile> skin) {
    //Null check
    Objects.requireNonNull(skin);

    //Return if item is not a player head.
    if (this.itemStack().getType() != Material.PLAYER_HEAD) {
      //Log
      log.warn("Skull texture can't be set on item={}.", this.itemStack().getType());
      return this;
    }

    //Return if no skull meta is present.
    if (!(this.itemStack().getItemMeta() instanceof SkullMeta skullMeta)) {
      //Log
      log.warn("Skull texture can't be set on item, no meta present.");
      return this;
    }

    //Option to set texture.
    if (skin.origin() == SkinOrigin.PROFILE_NAME) {
      //Set profile skin for player name.
      skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skin.value()));
    } else {
      //Create profile and set on skull.
      skullMeta.setPlayerProfile(skin.createProfile());
    }

    //Return
    return this;
  }
}
