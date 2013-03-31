package com.me.tft_02.duel;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.me.tft_02.duel.listeners.PlayerListener;
import com.me.tft_02.duel.runnables.DuelRangeTask;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Duel extends JavaPlugin {
    public static Duel instance;

    private final PlayerListener playerListener = new PlayerListener(this);

    public boolean worldGuardEnabled = false;

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
        setupConfiguration();
        setupWorldGuard();

        getCommand("duel").setExecutor(new Commands(this));

        BukkitScheduler scheduler = getServer().getScheduler();
        if (worldGuardEnabled) {
            //Region check timer (Runs every five seconds)
            scheduler.scheduleSyncRepeatingTask(this, new DuelRangeTask(), 0, 5 * 20);
        }
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();

        config.options().copyDefaults(true);
        saveConfig();
    }

    private void setupWorldGuard() {
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard found!");
        }
    }

    WorldGuardPlugin getWorldGuard() {
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
        this.getServer().getScheduler().cancelTasks(this);
    }
}
