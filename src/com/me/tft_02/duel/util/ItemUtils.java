package com.me.tft_02.duel.util;

import org.bukkit.inventory.ItemStack;
import com.me.tft_02.duel.Duel;

public class ItemUtils {
    Duel plugin;

    public ItemUtils(Duel instance) {
        plugin = instance;
    }

    /**
     * Checks if the item is a sword.
     *
     * @param is Item to check
     * @return true if the item is a sword, false otherwise
     */
    public static boolean isSword(ItemStack is) {
        switch (is.getType()) {
            case DIAMOND_SWORD:
            case GOLD_SWORD:
            case IRON_SWORD:
            case STONE_SWORD:
            case WOOD_SWORD:
                return true;

            default:
                return false;
        }
    }
}
