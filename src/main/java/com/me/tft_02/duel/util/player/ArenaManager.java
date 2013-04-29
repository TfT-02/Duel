package com.me.tft_02.duel.util.player;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ArenaManager {

    public static HashMap<String, Location> arenas = new HashMap<String, Location>();

    public static void setArena(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 0.5);

        arenas.put(player.getName(), location);
    }

    public static Location getArenaLocation(Player player) {
        Location location = arenas.get(player.getName());

        return location;
    }
}
