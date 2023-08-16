package dev.dotspace.network.library.spigot.itemstack;

import dev.dotspace.network.library.game.itemstack.GameEnchantmentInfo;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@Accessors(fluent = true)
public final class EnchantmentInfo implements IEnchantmentInfo {
    /**
     * See {@link GameEnchantmentInfo#type()}
     */
    private final @NotNull Enchantment type;
    /**
     * See {@link GameEnchantmentInfo#level()}
     */
    private final int level;
    /**
     * See {@link GameEnchantmentInfo#legal()}
     */
    private final boolean legal;

    /**
     * Construct instance.
     *
     * @param type  bukkit {@link EnchantmentInfo} to be bound to the item
     * @param level The level, which should receive the enchantment
     * @param legal if true using checks.
     */
    public EnchantmentInfo(@Nullable final Enchantment type,
                           final int level,
                           final boolean legal) {
        this.type = Objects.requireNonNull(type);
        this.level = level;
        this.legal = legal;
    }

    /**
     * Legal is set to false as default.
     */
    public EnchantmentInfo(@Nullable final Enchantment type,
                           final int level) {
        this(type, level, false);
    }

    /**
     * Level is set to 1 and legal to false as default.
     */
    public EnchantmentInfo(@Nullable final Enchantment type) {
        this(type, 1, false);
    }
}
