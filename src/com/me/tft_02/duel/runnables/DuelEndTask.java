package com.me.tft_02.duel.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.DuelManager;

public class DuelEndTask extends BukkitRunnable {
    private Player player;
    private Player target;

    public DuelEndTask(Player player, Player target) {
        this.player = player;
        this.target = target;
    }

    @Override
    public void run() {
        DuelManager.endDuel(player, target);
    }
}
