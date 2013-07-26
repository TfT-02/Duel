package com.me.tft_02.duel;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.mcstats.Metrics;

import com.me.tft_02.duel.commands.DuelCommand;
import com.me.tft_02.duel.database.Data;
import com.me.tft_02.duel.hooks.GhostsListener;
import com.me.tft_02.duel.hooks.WorldGuardListener;
import com.me.tft_02.duel.listeners.EntityListener;
import com.me.tft_02.duel.listeners.PlayerListener;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.runnables.DuelRangeTask;
import com.me.tft_02.duel.runnables.RegionCheckTask;
import com.me.tft_02.duel.runnables.UpdateCheckerTask;
import com.me.tft_02.duel.util.player.UserManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Duel extends JavaPlugin {
    public static Duel p;

    public boolean worldGuardEnabled = false;
    public boolean ghostsEnabled = false;

    // Update Check
    public boolean updateAvailable;

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        p = this;

        registerEvents();
        setupConfiguration();

        setupWorldGuard();
        setupGhosts();

        getCommand("duel").setExecutor(new DuelCommand());

        Data.loadData();

        for (Player player : getServer().getOnlinePlayers()) {
            UserManager.addUser(player); // In case of reload add all users back into UserManager
        }

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
            catch (IOException e) {}
        }
    }

    /**
     * Registers all event listeners
     */
    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Register events
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new EntityListener(), this);
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
            getServer().getPluginManager().registerEvents(new WorldGuardListener(this), this);
        }
    }

    private void setupGhosts() {
        if (getServer().getPluginManager().isPluginEnabled("Ghosts")) {
            ghostsEnabled = true;
            getLogger().info("Ghosts found!");
            getServer().getPluginManager().registerEvents(new GhostsListener(), this);
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
        if (!Config.getUpdateCheckEnabled()) {
            return;
        }

        getServer().getScheduler().runTaskAsynchronously(this, new UpdateCheckerTask());
    }

    public void updateCheckerCallback(boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
        if (updateAvailable) {
            getLogger().info(LocaleLoader.getString("UpdateChecker.outdated"));
            getLogger().info(LocaleLoader.getString("UpdateChecker.newavailable"));
        }
    }
}
