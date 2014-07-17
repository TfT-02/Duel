package com.me.tft_02.duel.runnables;

import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.locale.LocaleLoader;

import net.gravitydevelopment.updater.duel.Updater;
import net.gravitydevelopment.updater.duel.Updater.UpdateResult;
import net.gravitydevelopment.updater.duel.Updater.UpdateType;

public class UpdaterResultAsyncTask extends BukkitRunnable {
    private Duel plugin;

    public UpdaterResultAsyncTask(Duel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Updater updater = new Updater(plugin, 55022, Duel.duel, UpdateType.NO_DOWNLOAD, false);

        if (updater.getResult() != UpdateResult.UPDATE_AVAILABLE) {
            plugin.setUpdateAvailable(false);
            return;
        }

        if (updater.getLatestType().equals("beta") && !Config.getInstance().getPreferBeta()) {
            plugin.setUpdateAvailable(false);
            return;
        }

        plugin.setUpdateAvailable(true);
        plugin.getLogger().info(LocaleLoader.getString("UpdateChecker.Outdated"));
        plugin.getLogger().info(LocaleLoader.getString("UpdateChecker.New_Available"));
    }
}
