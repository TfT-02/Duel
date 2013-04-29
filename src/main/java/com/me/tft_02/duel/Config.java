package com.me.tft_02.duel;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    static FileConfiguration config = Duel.getInstance().getConfig();

    /* @formatter:off */
    /* GENERAL SETTINGS */
    public static boolean getStatsTrackingEnabled() { return config.getBoolean("General.Stats_Tracking_Enabled", true); }
    public static boolean getUpdateCheckEnabled() { return config.getBoolean("General.Update_Check_Enabled", true); }

    /* DUEL SETTINGS */
    public static boolean getPreventPVP() { return config.getBoolean("Duel.Prevent_PVP", false); }
    public static boolean getOverridePVP() { return config.getBoolean("Duel.Override_PVP", true); }
    public static boolean getSaveInventory() { return config.getBoolean("Duel.Save_Inventory", true); }
    public static int getInviteTimeout() { return config.getInt("Duel.Invitation_Timeout", 30); }
    public static int getDuelLength() { return config.getInt("Duel.Duel_Length", 120); }

    /* ARENA SETTINGS */
    public static boolean getBorderKnockBack() { return config.getBoolean("Arena.Knockback_Enabled", true); }
    public static double getArenaSize() { return config.getDouble("Arena.Radius", 20.0); }
    public static boolean getBorderParticleEffectsEnabled() { return false; }

    /* @formatter:on */
}
