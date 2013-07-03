package com.me.tft_02.duel;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.me.tft_02.duel.commands.DuelCommand;
import com.me.tft_02.duel.database.Data;
import com.me.tft_02.duel.hooks.WorldGuardListener;
import com.me.tft_02.duel.listeners.EntityListener;
import com.me.tft_02.duel.listeners.PlayerListener;
import com.me.tft_02.duel.runnables.DuelRangeTask;
import com.me.tft_02.duel.runnables.RegionCheckTask;
import com.me.tft_02.duel.util.Metrics;
import com.me.tft_02.duel.util.UpdateChecker;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Duel extends JavaPlugin {
    public static Duel instance;

    private final PlayerListener playerListener = new PlayerListener(this);
    private final EntityListener entityListener = new EntityListener(this);

    // Listeners of dependencies
    private WorldGuardListener worldGuardListener = new WorldGuardListener(this);

    public boolean worldGuardEnabled = false;

    // Update Check
    public boolean updateAvailable;

    public static Duel getInstance() {
        return instance;
    }

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        instance = this;
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);

        setupConfiguration();

        setupWorldGuard();

        getCommand("duel").setExecutor(new DuelCommand());

        Data.loadData();

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new DuelRangeTask(), 0, 2 * 20);

        if (worldGuardEnabled) {
            //Region check timer (Runs every five seconds)
            new RegionCheckTask().runTaskTimer(this, 5 * 20, 5 * 20);
        }

        checkForUpdates();

        if (Config.getStatsTrackingEnabled()) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            }
            catch (IOException e) {
                System.out.println("Failed to submit stats.");
            }
        }
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();

        /* GENERAL SETTINGS */
        config.addDefault("General.Stats_Tracking_Enabled", true);
        config.addDefault("General.Update_Check_Enabled", true);

        /* DUEL SETTINGS */
        config.addDefault("Duel.Prevent_PVP", false);
        config.addDefault("Duel.Override_PVP", true);
        config.addDefault("Duel.Save_Inventory", true);
        config.addDefault("Duel.Invitation_Timeout", 30);
        config.addDefault("Duel.Duel_Length", 120);
        config.addDefault("Duel.Message_Range", 100);

        /* ARENA SETTINGS */
        config.addDefault("Arena.Knockback_Enabled", true);
        config.addDefault("Arena.Radius", 20.0);

        /* WORLDGUARD SETTINGS */
        config.addDefault("WorldGuard.Use_As_Whitelist", false);

        String[] defaultRegions = { "no_duels_here" };
        config.addDefault("WorldGuard.Regions", defaultRegions);

        config.options().copyDefaults(true);
        saveConfig();
    }

    private void setupWorldGuard() {
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard found!");
            getServer().getPluginManager().registerEvents(worldGuardListener, this);
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {
        Data.saveData();
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void checkForUpdates() {
        if (Config.getUpdateCheckEnabled()) {
            try {
                updateAvailable = UpdateChecker.updateAvailable();
            }
            catch (Exception e) {
                updateAvailable = false;
            }

            if (updateAvailable) {
                this.getLogger().log(Level.INFO, ChatColor.GOLD + "Duel is outdated!");
                this.getLogger().log(Level.INFO, ChatColor.AQUA + "http://dev.bukkit.org/server-mods/duel/");
            }
        }
    }
}
