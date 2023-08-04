package dev.dotspace.network.library.game.itemstack;

import org.jetbrains.annotations.NotNull;

public interface IEnchantmentInfo<ENCHANTMENT> {
    /**
     * Type of enchantment.
     *
     * @return instance of implementation enchantment.
     */
    @NotNull ENCHANTMENT type();

    /**
     * The level, which should receive the enchantment
     *
     * @return level.
     */
    int level();

    /**
     * Some implementations check fi given type and level are valid.
     * If this boolean is false, this check will be ignored.
     *
     * @return value if enchantment has to be legal
     */
    boolean legal();
}
