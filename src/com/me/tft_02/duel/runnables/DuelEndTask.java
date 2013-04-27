package com.me.tft_02.duel.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.util.player.DuelManager;

public class DuelEndTask extends BukkitRunnable {
    private Player player;

    public DuelEndTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (PlayerData.isInDuel(player)) {
            DuelManager.endDuelInTie(player);
        }
    }
}
