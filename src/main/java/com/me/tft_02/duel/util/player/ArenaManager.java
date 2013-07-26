package com.me.tft_02.duel.util.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ArenaManager {

    public static void setArena(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 0.5);

        UserManager.getPlayer(player).setArena(location);
    }

    public static Location getArenaLocation(Player player) {
        return UserManager.getPlayer(player).getArena();
    }

    public static void deleteArena(Player player) {
        UserManager.getPlayer(player).setArena(null);
    }
}
