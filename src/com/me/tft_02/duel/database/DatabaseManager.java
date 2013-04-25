package com.me.tft_02.duel.database;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class DatabaseManager {

    public static HashMap<String, Integer> wins = new HashMap<String, Integer>();
    public static HashMap<String, Integer> losses = new HashMap<String, Integer>();
    public static HashMap<String, Integer> ties = new HashMap<String, Integer>();

    public static int getWinCount(Player player) {
        int count = 0;
        if (wins.containsKey(player.getName())) {
            count = wins.get(player.getName());
        }

        return count;
    }
    
    public static void setWinCount(Player player, int count) {
        wins.put(player.getName(), count);
    }

    public static void increaseWinCount(Player player, int amount) {
        int count = getWinCount(player);

        setWinCount(player, count + amount);
    }

    public static int getLossCount(Player player) {
        int count = 0;
        if (losses.containsKey(player.getName())) {
            count = losses.get(player.getName());
        }

        return count;
    }
    
    public static void setLossCount(Player player, int count) {
        losses.put(player.getName(), count);
    }

    public static void increaseLossCount(Player player, int amount) {
        int count = getLossCount(player);

        setLossCount(player, count + amount);
    }

    public static int getTieCount(Player player) {
        int count = 0;
        if (ties.containsKey(player.getName())) {
            count = ties.get(player.getName());
        }

        return count;
    }
    
    public static void setTieCount(Player player, int count) {
        ties.put(player.getName(), count);
    }

    public static void increaseTieCount(Player player, int amount) {
        int count = getTieCount(player);

        setTieCount(player, count + amount);
    }
}