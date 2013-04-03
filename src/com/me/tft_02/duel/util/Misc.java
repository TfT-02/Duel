package com.me.tft_02.duel.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.Duel;

public class Misc {

    /**
     * Determine if two locations are near each other.
     * 
     * @param first The first location
     * @param second The second location
     * @param maxDistance The max distance apart
     * @return true if the distance between <code>first</code> and <code>second</code> is less than <code>maxDistance</code>, false otherwise
     */
    public static boolean isNear(Location first, Location second, double maxDistance) {
        if (!first.getWorld().equals(second.getWorld())) {
            return false;
        }

        return first.distanceSquared(second) < (maxDistance * maxDistance);

    }
    
    public static List<Player> getNearbyPlayers(Location location) {
        List<Player> nearbyPlayers = new ArrayList<Player>();

        for (Player player : Duel.getInstance().getServer().getOnlinePlayers()) {
            if (Misc.isNear(location, player.getLocation(), 100)) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }
}
