package com.me.tft_02.duel.util;

import org.bukkit.inventory.ItemStack;

import com.me.tft_02.duel.Config;
import com.me.tft_02.duel.Duel;

public class ItemUtils {
    Duel plugin;

    public ItemUtils(Duel instance) {
        plugin = instance;
    }

    /**
     * Checks if the item is a duel item.
     *
     * @param is Item to check
     * @return true if the item is a duel item, false otherwise
     */
    public static boolean isDuelWeapon(ItemStack item) {
        return Config.getDuelWeaponItems().contains(item.getType());
    }
}
