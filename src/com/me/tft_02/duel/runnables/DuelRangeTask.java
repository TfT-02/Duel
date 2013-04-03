package com.me.tft_02.duel.runnables;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.DuelManager;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.PlayerData;

public class DuelRangeTask extends BukkitRunnable {

    @Override
    public void run() {
        checkDuelingPlayers();
    }

    private void checkDuelingPlayers() {
        for (Player player : PlayerData.getDuelingPlayers()) {
            Player target = PlayerData.getDuelTarget(player);
            if (Misc.isNear(player.getLocation(), target.getLocation(), 50)) {
                player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            } else {
                DuelManager.endDuel(player, target);
            }
        }
    }
}
