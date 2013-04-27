package com.me.tft_02.duel;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    static FileConfiguration config = Duel.getInstance().getConfig();

    /* GENERAL SETTINGS */
    public static boolean getStatsTrackingEnabled() {
        return config.getBoolean("General.stats_tracking_enabled", true);
    }

    public static boolean getUpdateCheckEnabled() {
        return config.getBoolean("General.update_check_enabled", true);
    }

    /* ARENA SETTINGS */
    public static boolean getBorderKnockBack() {
        return config.getBoolean("Duel.Knockback_Enabled", true);
    }

    public static boolean getPreventPVP() {
        return config.getBoolean("Duel.Prevent_PVP", false);
    }

    public static boolean getOverridePVP() {
        return config.getBoolean("Duel.Override_PVP", true);
    }

    public static double getArenaSize() {
        return config.getDouble("Duel.Arena_Size", 20.0);
    }

    public static boolean getSaveInventory() {
        return config.getBoolean("Duel.Save_Inventory", true);
    }

    public static boolean getBorderParticleEffectsEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
}
