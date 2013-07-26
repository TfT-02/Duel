package com.me.tft_02.duel;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    static FileConfiguration config = Duel.p.getConfig();

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
    public static int getMessageRange() { return config.getInt("Duel.Message_Range", 100); }

    /* ARENA SETTINGS */
    public static boolean getBorderKnockBack() { return config.getBoolean("Arena.Knockback_Enabled", true); }
    public static double getArenaSize() { return config.getDouble("Arena.Radius", 20.0); }
    public static boolean getBorderParticleEffectsEnabled() { return false; }

    /* @formatter:on */

    public static HashSet<Material> getDuelWeaponItems() {
        HashSet<Material> miscItems = new HashSet<Material>();

        for (String item : config.getStringList("Challenge.Duel_Items")) {
            Material material = Material.getMaterial(item.toUpperCase());

            if (material != null) {
                miscItems.add(material);
            }
        }
        return miscItems;
    }

    /* WORLDGUARD SETTINGS */
    public static boolean getUseAsWhitelist() {
        return config.getBoolean("WorldGuard.Use_As_Whitelist", false);
    }

    public static List<String> getRegionList() {
        return Duel.p.getConfig().getStringList("WorldGuard.Regions");
    }
}
