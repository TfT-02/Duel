package com.me.tft_02.duel.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.me.tft_02.duel.Duel;

public class Misc {

    public static int getSystemTime() {
        return (int) System.currentTimeMillis() / 1000;
    }

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

    public static Vector getKnockbackVector(Location first, Location second) {
        double dX = first.getX() - second.getX();
        double dY = first.getY() - second.getY();
        double dZ = first.getZ() - second.getZ();

        double yaw = Math.atan2(dZ, dX);

        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;

        double X = Math.sin(pitch) * Math.cos(yaw);
        double Y = Math.sin(pitch) * Math.sin(yaw);
        double Z = Math.cos(pitch);

        Vector vector = new Vector(X, Z, Y);
        return vector;
    }

    public static List<Player> getNearbyPlayers(Location location, int range) {
        List<Player> nearbyPlayers = new ArrayList<Player>();

        for (Player player : Duel.getInstance().getServer().getOnlinePlayers()) {
            if (Misc.isNear(location, player.getLocation(), range)) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }
}
