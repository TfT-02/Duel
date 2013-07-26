package com.me.tft_02.duel.util.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.datatypes.player.DuelPlayer;

public final class UserManager {
    private final static Map<String, DuelPlayer> players = new HashMap<String, DuelPlayer>();

    private UserManager() {};

    /**
     * Add a new user.
     *
     * @param player The player to create a user record for
     * @return the player's {@link DuelPlayer} object
     */
    public static DuelPlayer addUser(Player player) {
        String playerName = player.getName();
        DuelPlayer duelPlayer = players.get(playerName);

        if (duelPlayer != null) {
            duelPlayer.setPlayer(player); // The player object is different on each reconnection and must be updated
        }
        else {
            duelPlayer = new DuelPlayer(player);
            players.put(playerName, duelPlayer);
        }

        return duelPlayer;
    }

    /**
     * Remove a user.
     *
     * @param playerName The name of the player to remove
     */
    public static void remove(String playerName) {
        players.remove(playerName);
    }

    /**
     * Clear all users.
     */
    public static void clearAll() {
        players.clear();
    }

    /**
     * Save all users.
     */
    //    public static void saveAll() {
    //        for (DuelPlayer duelPlayer : players.values()) {
    //            duelPlayer.getProfile().save();
    //        }
    //    }

    public static Set<String> getPlayerNames() {
        return players.keySet();
    }

    public static Collection<DuelPlayer> getPlayers() {
        return players.values();
    }

    /**
     * Get the DuelPlayer of a player by a partial name.
     *
     * @param playerName The partial name of the player whose DuelPlayer to retrieve
     * @return the player's DuelPlayer object
     */
    public static DuelPlayer getPlayer(String playerName) {
        List<Player> matches = Duel.p.getServer().matchPlayer(playerName);

        if (matches.size() == 1) {
            playerName = matches.get(0).getName();
        }

        return players.get(playerName);
    }

    /**
     * Get the DuelPlayer of a player.
     *
     * @param player The player whose DuelPlayer to retrieve
     * @return the player's DuelPlayer object
     */
    public static DuelPlayer getPlayer(OfflinePlayer player) {
        return players.get(player.getName());
    }
}
