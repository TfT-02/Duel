package com.me.tft_02.duel.runnables.duels;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.RegionUtils;
import com.me.tft_02.duel.util.player.DuelManager;

public class CountdownTask extends BukkitRunnable {
    private Player player;
    private Player target;
    private int number;

    public CountdownTask(Player player, Player target, int number) {
        this.player = player;
        this.target = target;
        this.number = number;
    }

    @Override
    public void run() {
        countdown();
    }

    private void countdown() {
        if (number > 0) {
            if (!areValid(player, target)) {
                return;
            }

            if (!checkWGRegion()) {
                return;
            }

            player.sendMessage(LocaleLoader.getString("Duel.Countdown", number));
            target.sendMessage(LocaleLoader.getString("Duel.Countdown", number));
            player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 0F);

            number--;
        }
        else {
            DuelManager.startDuel(player, target);
            this.cancel();
        }
    }

    private boolean areValid(Player player, Player target) {
        return isValid(player, target) && isValid(target, player);
    }

    private boolean isValid(Player player, Player target) {
        if (target == null || !target.isValid()) {
            player.sendMessage(LocaleLoader.getString("Duel.Countdown.Cancelled.Left"));
            player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS, 1F, 0F);
            DuelManager.endDuel(player, true);
            this.cancel();
            return false;
        }

        return true;
    }

    private boolean checkWGRegion() {
        if (!RegionUtils.canDuelHereWG(player.getLocation()) || !RegionUtils.canDuelHereWG(target.getLocation())) {
            player.sendMessage(LocaleLoader.getString("Duel.Cancelled.WorldGuard"));
            target.sendMessage(LocaleLoader.getString("Duel.Cancelled.WorldGuard"));
            player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS, 1F, 0F);
            DuelManager.endDuel(player, true);
            DuelManager.endDuel(target, true);
            this.cancel();
            return false;
        }

        return true;
    }
}
