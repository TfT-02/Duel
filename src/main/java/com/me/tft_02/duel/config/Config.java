package com.me.tft_02.duel.config;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;

import com.me.tft_02.duel.Duel;

public class Config extends AutoUpdateConfigLoader {
    private static  Config instance;

    private Config() {
        super("config.yml");
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    @Override
    protected void loadKeys() {}

    /* @formatter:off */

    /* GENERAL SETTINGS */
    public String getLocale() { return config.getString("General.Locale", "en_us"); }
    public int getSaveInterval() { return config.getInt("General.Save_Interval", 10); }
    public boolean getStatsTrackingEnabled() { return config.getBoolean("General.Stats_Tracking", true); }
    public boolean getUpdateCheckEnabled() { return config.getBoolean("General.Update_Check", true); }
    public boolean getPreferBeta() { return config.getBoolean("General.Prefer_Beta", false); }
    public boolean getVerboseLoggingEnabled() { return config.getBoolean("General.Verbose_Logging", false); }
    public boolean getConfigOverwriteEnabled() { return config.getBoolean("General.Config_Update_Overwrite", true); }

    /* DUEL SETTINGS */
    public  boolean getPreventPVP() { return config.getBoolean("Duel.Prevent_PVP", false); }
    public  boolean getOverridePVP() { return config.getBoolean("Duel.Override_PVP", true); }
    public  boolean getSaveInventory() { return config.getBoolean("Duel.Save_Inventory", true); }
    public  int getDuelLength() { return config.getInt("Duel.Duel_Length", 120); }
    public  int getMessageRange() { return config.getInt("Duel.Message_Range", 100); }
    public  boolean getHealEnabled() { return config.getBoolean("Duel.Heal_After_Duel", true); }

    /* ARENA SETTINGS */
    public  boolean getBorderKnockBack() { return config.getBoolean("Arena.Knockback_Enabled", true); }
    public  double getArenaSize() { return config.getDouble("Arena.Radius", 20.0); }
    public  boolean getBorderParticleEffectsEnabled() { return false; }

    /* CHALLENGE SETTINGS */
    public  int getInviteTimeout() { return config.getInt("Challenge.Timeout", 30); }
    public  HashSet<Material> getDuelWeaponItems() {
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
    public  boolean getUseAsWhitelist() { return config.getBoolean("WorldGuard.Use_As_Whitelist", false); }
    public  List<String> getRegionList() { return Duel.p.getConfig().getStringList("WorldGuard.Regions"); }

    /* @formatter:on */
}
