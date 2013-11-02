package com.me.tft_02.duel.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.me.tft_02.duel.Duel;

public class Misc {
    public static final int TIME_CONVERSION_FACTOR = 1000;
    public static final int TICK_CONVERSION_FACTOR = 20;

    public static boolean isNPCEntity(Entity entity) {
        return (entity == null || entity.hasMetadata("NPC"));
    }

    public static int getSystemTime() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static boolean cooldownExpired(long deactivatedTimeStamp, int cooldown) {
        return (System.currentTimeMillis() >= (deactivatedTimeStamp + cooldown) * Misc.TIME_CONVERSION_FACTOR);
    }

    /**
     * Determine if two locations are near each other.
     *
     * @param first       The first location
     * @param second      The second location
     * @param maxDistance The max distance apart
     *
     * @return true if the distance between <code>first</code> and <code>second</code> is less than <code>maxDistance</code>, false otherwise
     */
    public static boolean isNear(Location first, Location second, double maxDistance) {
        if (!first.getWorld().equals(second.getWorld())) {
            return false;
        }

        return first.distanceSquared(second) < (maxDistance * maxDistance);
    }

    public static Location getMiddle(Location first, Location second) {
        double dX = first.getX() - second.getX();
        double dZ = first.getZ() - second.getZ();
        double dY = first.getY() - second.getY();

        double x = first.getX() + dX;
        double z = first.getZ() + dZ;
        double y = first.getY() + dY;

        World world = first.getWorld();

        while (world.getBlockAt((int) x, (int) y, (int) z).getType() != Material.AIR) {
            y++;
        }

        return new Location(world, x, y, z);
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

        return new Vector(X, Z, Y);
    }

    public static List<Player> getNearbyPlayers(Location location, int range) {
        List<Player> nearbyPlayers = new ArrayList<Player>();

        for (Player player : Duel.p.getServer().getOnlinePlayers()) {
            if (Misc.isNear(location, player.getLocation(), range)) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }

    /**
     * Attempts to match any player names with the given name, and returns a list of all possibly matches.
     * <p/>
     * This list is not sorted in any particular order.
     * If an exact match is found, the returned list will only contain a single result.
     *
     * @param partialName Name to match
     *
     * @return List of all possible names
     */
    public static List<String> matchPlayer(String partialName) {
        List<String> matchedPlayers = new ArrayList<String>();

        for (OfflinePlayer offlinePlayer : Duel.p.getServer().getOfflinePlayers()) {
            String playerName = offlinePlayer.getName();

            if (partialName.equalsIgnoreCase(playerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(playerName);
                break;
            }
            if (playerName.toLowerCase().contains(partialName.toLowerCase())) {
                // Partial match
                matchedPlayers.add(playerName);
            }
        }

        return matchedPlayers;
    }

    /**
     * Get a matched player name if one was found in the database.
     *
     * @param partialName Name to match
     *
     * @return Matched name or {@code partialName} if no match was found
     */
    public static String getMatchedPlayerName(String partialName) {
        List<String> matches = matchPlayer(partialName);

        if (matches.size() == 1) {
            partialName = matches.get(0);
        }

        return partialName;
    }
}
