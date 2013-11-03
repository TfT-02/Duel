package com.me.tft_02.duel.runnables.player;

import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.datatypes.player.DuelPlayer;
import com.me.tft_02.duel.datatypes.player.PlayerData;

public class RetrieveLevelsTask extends BukkitRunnable {
    private DuelPlayer duelPlayer;

    public RetrieveLevelsTask(DuelPlayer duelPlayer) {
        this.duelPlayer = duelPlayer;
    }

    @Override
    public void run() {
        PlayerData.retrieveLevelsAndExp(duelPlayer);
    }
}
