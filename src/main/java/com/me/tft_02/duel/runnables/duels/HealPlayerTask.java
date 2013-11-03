package com.me.tft_02.duel.runnables.duels;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HealPlayerTask extends BukkitRunnable {
    private Player player;

    public HealPlayerTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isValid()) {
            player.setHealth(player.getMaxHealth());
            player.setFireTicks(0);
        }
    }
}
