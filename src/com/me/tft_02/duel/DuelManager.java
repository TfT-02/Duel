package com.me.tft_02.duel;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.PlayerData;

public class DuelManager {
    public enum DuelMessageType {
        START,
        END;
    };

    public static void notifyPlayers(Location location, DuelMessageType messageType) {
        String message = "";
        switch (messageType) {
            case END:
                message = "Duel has ended!";
                break;
            case START:
                message = "Duel has started!";
                break;
            default:
                break;
        }

        for (Player player : Misc.getNearbyPlayers(location)) {
            player.sendMessage(message);
        }

    }

    public static void endDuel(Player player, Player target) {
        PlayerData.removeDuelTarget(player);
        PlayerData.removeDuelTarget(target);
        notifyPlayers(player.getLocation(), DuelMessageType.END);
    }
}
