package com.me.tft_02.duel.util.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.Config;
import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.database.DatabaseManager;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.runnables.CountdownTask;
import com.me.tft_02.duel.runnables.DuelCommenceTask;
import com.me.tft_02.duel.runnables.DuelEndTask;
import com.me.tft_02.duel.util.Misc;

public class DuelManager {
    public enum DuelMessageType {
        START,
        END;
    };

    public static void notifyPlayers(Location location, DuelMessageType messageType) {
        String message = "";
        switch (messageType) {
            case END:
                message = ChatColor.GOLD + "Duel has ended!";
                break;
            case START:
                message = ChatColor.GOLD + "Duel has started!";
                break;
            default:
                break;
        }

        for (Player player : Misc.getNearbyPlayers(location)) {
            player.sendMessage(message);
        }

    }

    public static void notifyPlayers(Location location, String message) {
        for (Player player : Misc.getNearbyPlayers(location)) {
            player.sendMessage(message);
        }
    }

    public static void handleDuelInvites(Player player, Player target) {
        PlayerData playerData = new PlayerData();

        if (playerData.getDuelInvite(player).equals(target.getName())) {
            if (playerData.duelInviteIsTimedout(player)) {
                player.sendMessage(ChatColor.RED + "The Duel invitation has expired.");
                playerData.removeDuelInvite(player);
                return;
            }

            DuelManager.prepareDuel(player, target);
            new CountdownTask(player.getLocation(), 4).runTaskTimer(Duel.getInstance(), 0, 1 * 20);
            new DuelCommenceTask(player, target).runTaskLater(Duel.getInstance(), 4 * 20);
        }
        else {
            playerData.setDuelInvite(player, target);
        }
    }

    public static void prepareDuel(Player player, Player target) {
        PlayerData playerData = new PlayerData();
        playerData.setDuelInviteNone(player);
        playerData.setDuelInviteNone(target);

        ArenaManager.setArena(player);
        ArenaManager.setArena(target);
    }

    public static void startDuel(Player player, Player target) {
        if (!(player.isOnline() && target.isOnline())) {
            return;
        }

        PlayerData playerData = new PlayerData();
        playerData.removeDuelInvite(player);
        playerData.removeDuelInvite(target);
        playerData.setDuel(player, target);
        notifyPlayers(player.getLocation(), DuelMessageType.START);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);

        int duelLength = Config.getDuelLength();
        new DuelEndTask(player).runTaskLater(Duel.getInstance(), duelLength * 20);
    }

    public static void endDuel(Player winner, Player loser) {
        PlayerData.removeDuelTarget(winner);
        PlayerData.removeDuelTarget(loser);
        notifyPlayers(winner.getLocation(), DuelMessageType.END);

        DatabaseManager.increaseWinCount(winner, 1);
        DatabaseManager.increaseLossCount(loser, 1);

        loser.playSound(loser.getLocation(), Sound.WITHER_SPAWN, 1F, 0F);
        winner.playSound(loser.getLocation(), Sound.WITHER_SPAWN, 1F, 0F);
    }

    public static void endDuelInTie(Player player) {
        Player target = PlayerData.getDuelTarget(player);

        PlayerData.removeDuelTarget(player);
        PlayerData.removeDuelTarget(target);
        notifyPlayers(player.getLocation(), DuelMessageType.END);

        DatabaseManager.increaseTieCount(player, 1);
        DatabaseManager.increaseTieCount(target, 1);
    }
}
