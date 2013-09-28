package com.me.tft_02.duel.runnables.duels;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.player.DuelManager;

public class CountdownLocationTask extends BukkitRunnable {
    private Location location;
    private int number;

    public CountdownLocationTask(Location location, int number) {
        this.location = location;
        this.number = number;
    }

    @Override
    public void run() {
        countdown();
    }

    private void countdown() {
        if (number > 0) {
            DuelManager.notifyNearbyPlayers(location, LocaleLoader.getString("Duel.Countdown", number));
            location.getWorld().playSound(location, Sound.NOTE_PLING, 1F, 0F);

            if (number <= 1) {
                this.cancel();
            }

            number--;
        }
    }
}
