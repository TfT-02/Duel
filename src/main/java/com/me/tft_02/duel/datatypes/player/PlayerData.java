package com.me.tft_02.duel.datatypes.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.datatypes.DuelInvitationKey;
import com.me.tft_02.duel.datatypes.LevelAndExpKey;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.Misc;

public class PlayerData {

    public static HashMap<String, String> duels = new HashMap<String, String>();

    public void setDuel(Player player, Player target) {
        duels.put(player.getName(), target.getName());
        duels.put(target.getName(), player.getName());
    }

    public static String getDuelTargetName(Player player) {
        String target = "null";
        if (duels.containsKey(player.getName())) {
            target = duels.get(player.getName());
        }
        return target;
    }

    public static Player getDuelTarget(Player player) {
        String targetName = getDuelTargetName(player);
        return Duel.p.getServer().getPlayer(targetName);
    }

    public static boolean removeDuelTarget(Player player) {
        if (duels.containsKey(player.getName())) {
            duels.remove(player.getName());
            return true;
        }
        return false;
    }

    public static boolean isInDuel(Player player) {
        if (getDuelTarget(player) == null || getDuelTarget(player).equals("null")) {
            return false;
        }
        return true;
    }

    public static boolean areDueling(Player player, Player target) {
        if (player == null || target == null) {
            return false;
        }

        Player duelTargetPlayer = getDuelTarget(target);
        Player duelTargetTarget = getDuelTarget(player);

        if (duelTargetPlayer == null || duelTargetTarget == null) {
            return false;
        }

        if (player.getName().equals(duelTargetPlayer.getName()) && target.getName().equals(duelTargetTarget.getName())) {
            return true;

        }
        return false;

    }

    public String getDuelInvite(DuelPlayer duelPlayer) {
        return duelPlayer.getDuelInvite().getPlayerName();
    }

    public boolean duelInviteIsTimedout(DuelPlayer duelPlayer) {
        DuelInvitationKey key = duelPlayer.getDuelInvite();
        if (key.getTimestamp() + Config.getInstance().getInviteTimeout() >= Misc.getSystemTime()) {
            return false;
        }

        return true;
    }

    public void removeDuelInvite(DuelPlayer duelPlayer) {
        duelPlayer.setDuelInvitationKey(null);
    }

    public void setDuelInvite(DuelPlayer duelPlayer, DuelPlayer duelTarget) {
        if (getDuelInvite(duelTarget).equals(duelPlayer.getPlayer().getName())) {
            return;
        }
        Player player = duelPlayer.getPlayer();
        Player target = duelTarget.getPlayer();

        player.sendMessage(LocaleLoader.getString("Duel.Invite.Send", target.getName()));
        duelTarget.setDuelInvitationKey(new DuelInvitationKey(player.getName(), Misc.getSystemTime()));

        target.sendMessage(LocaleLoader.getString("Duel.Invite.Receive.1", player.getName()));
        target.sendMessage(LocaleLoader.getString("Duel.Invite.Receive.2", player.getName()));
    }

    public void removeDuelInvitation(DuelPlayer duelPlayer) {
        duelPlayer.setDuelInvitationKey(null);
    }

    public static List<Player> getDuelingPlayers() {
        Player[] onlinePlayers = Duel.p.getServer().getOnlinePlayers();
        List<Player> duelingPlayers = new ArrayList<Player>();

        for (Player onlinePlayer : onlinePlayers) {
            if (duels.containsKey(onlinePlayer.getName())) {
                duelingPlayers.add(onlinePlayer);
            }
        }
        return duelingPlayers;
    }

    public static void storeInventory(DuelPlayer duelPlayer, List<ItemStack> items) {
        duelPlayer.setSavedInventoryItems(items);
    }

    public static List<ItemStack> retrieveInventory(DuelPlayer duelPlayer) {
        return duelPlayer.getSavedInventoryItems();
    }

    public static void storeArmor(DuelPlayer duelPlayer, List<ItemStack> items) {
        duelPlayer.setSavedInventoryArmor(items);
    }

    public static List<ItemStack> retrieveArmor(DuelPlayer duelPlayer) {
        return duelPlayer.getSavedInventoryArmor();
    }

    public static void storeLevelsAndExp(DuelPlayer duelPlayer) {
        Player player = duelPlayer.getPlayer();
        duelPlayer.setSavedLevel(new LevelAndExpKey(player.getLevel(), player.getExp()));
    }

    public static boolean retrieveLevelsAndExp(DuelPlayer duelPlayer) {
        Player player = duelPlayer.getPlayer();

        LevelAndExpKey key = duelPlayer.getSavedLevel();

        if (key == null) {
            return false;
        }

        player.setLevel(key.getLevel());
        player.setExp(key.getExp());
        return true;
    }
}
