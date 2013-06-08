package com.me.tft_02.duel.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.datatypes.player.PlayerData;

public class RetrieveLevelsTask extends BukkitRunnable {
    private Player player;

    public RetrieveLevelsTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        PlayerData.retrieveLevelsAndExp(player);
    }
}
