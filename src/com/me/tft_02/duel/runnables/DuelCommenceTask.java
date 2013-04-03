package com.me.tft_02.duel.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelCommenceTask extends BukkitRunnable {
    private Player player;

    public DuelCommenceTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        checkDuelCommence(player);
    }

    public void checkDuelCommence(Player player) {
        if (!player.hasPermission("duel.commence")) {
            return;
        }

        if (!player.isBlocking()) {
            return;
        }

        startDuel(player, player);
    }

    public void startDuel(Player player, Player target) {

    }
}
