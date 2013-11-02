package com.me.tft_02.duel.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;

public class Config extends AutoUpdateConfigLoader {
    private static  Config instance;

    private Config() {
        super("config.yml");
        validate();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    @Override
    protected boolean validateKeys() {
        // Validate all the settings!
        List<String> reason = new ArrayList<String>();

        /* General Settings */
        if (getSaveInterval() <= 0) {
            reason.add("General.Save_Interval should be greater than 0!");
        }

        /* Duel Settings */
        if (!(getChallengeCommandsEnabled() || getChallengeInteractEnabled())) {
            reason.add("Either Enable_Challenge_Commands or Enable_Challenge_Interact should be enabled!");
        }

        if (getDuelLength() <= 0) {
            reason.add("Duel.Duel_Length should be greater than 0!");
        }

        return noErrorsInConfig(reason);
    }

    @Override
    protected void loadKeys() {}

    /* @formatter:off */

    /* GENERAL SETTINGS */
    public String getLocale() { return config.getString("General.Locale", "en_us"); }
    public int getSaveInterval() { return config.getInt("General.Save_Interval", 10); }
    public boolean getUpdateCheckEnabled() { return config.getBoolean("General.Update_Check", true); }
    public boolean getPreferBeta() { return config.getBoolean("General.Prefer_Beta", false); }
    public boolean getVerboseLoggingEnabled() { return config.getBoolean("General.Verbose_Logging", false); }
    public boolean getConfigOverwriteEnabled() { return config.getBoolean("General.Config_Update_Overwrite", true); }

    /* DUEL SETTINGS */
    public boolean getPreventPVP() { return config.getBoolean("Duel.Prevent_PVP", false); }
    public boolean getOverridePVP() { return config.getBoolean("Duel.Override_PVP", true); }
    public boolean getSaveInventory() { return config.getBoolean("Duel.Save_Inventory", true); }
    public int getDuelLength() { return config.getInt("Duel.Duel_Length", 120); }
    public int getMessageRange() { return config.getInt("Duel.Message_Range", 50); }
    public boolean getHealEnabled() { return config.getBoolean("Duel.Heal_After_Duel", true); }
    public List<String> getPotionEffectsToRemove() { return config.getStringList("Duel.Potion_Effects_To_Remove"); }
    public List<String> getBlockedCommands() { return config.getStringList("Duel.Blocked_Commands"); }
    public boolean getCallDeathEvents() { return config.getBoolean("Duel.Call_DeathEvents", false); }

    /* ARENA SETTINGS */
    public boolean getBorderKnockBack() { return config.getBoolean("Arena.Knockback_Enabled", true); }
    public double getArenaSize() { return config.getDouble("Arena.Radius", 20.0); }
    public boolean getBorderParticleEffectsEnabled() { return false; }

    /* CHALLENGE SETTINGS */
    public boolean getChallengeCommandsEnabled() { return config.getBoolean("Challenge.Enable_Challenge_Commands", false); }
    public boolean getChallengeInteractEnabled() { return config.getBoolean("Challenge.Enable_Challenge_Interact", true); }
    public int getInviteTimeout() { return config.getInt("Challenge.Timeout", 20); }

    public HashSet<Material> getDuelWeaponItems() {
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
    public boolean getWGUseAsWhitelist() { return config.getBoolean("WorldGuard.Use_As_Whitelist", false); }
    public List<String> getWGRegionList() { return config.getStringList("WorldGuard.Regions"); }

    /* WORLD SETTINGS */
    public boolean getWorldUseAsWhitelist() { return config.getBoolean("Worlds.Use_As_Whitelist", false); }
    public List<String> getWorldList() { return config.getStringList("Worlds.Worlds"); }

    /* @formatter:on */
}
