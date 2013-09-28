package com.me.tft_02.duel.datatypes.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.datatypes.DuelInvitationKey;
import com.me.tft_02.duel.datatypes.LevelAndExpKey;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.player.UserManager;

public class PlayerData {

    public void setDuel(Player player, Player target) {
        UserManager.getPlayer(player).setTargetName(target.getName());
        UserManager.getPlayer(target).setTargetName(player.getName());
    }

    public static Player getDuelTarget(Player player) {
        String targetName = UserManager.getPlayer(player).getTargetName();

        if (targetName != null) {
            return Duel.p.getServer().getPlayer(targetName);
        }

        return null;
    }

    public static boolean removeDuelTarget(Player player) {
        DuelPlayer duelPlayer = UserManager.getPlayer(player);

        if (duelPlayer.getTargetName() != null) {
            duelPlayer.setTargetName(null);
            return true;
        }
        return false;
    }

    public static boolean isInDuel(Player player) {
        if (UserManager.getPlayer(player).getTargetName() == null) {
            return false;
        }
        return true;
    }

    public static boolean areDueling(Player player, Player target) {
        if (player == null || target == null) {
            return false;
        }

        DuelPlayer duelPlayer = UserManager.getPlayer(player);
        DuelPlayer duelTarget = UserManager.getPlayer(target);

        String playerTargetName = duelPlayer.getTargetName();
        String targetTargetName = duelTarget.getTargetName();

        if (playerTargetName == null || targetTargetName == null) {
            return false;
        }

        if (playerTargetName.equals(target.getName()) && targetTargetName.equals(player.getName())) {
            return true;
        }

        return false;
    }

    public String getDuelInvite(DuelPlayer duelPlayer) {
        if (duelPlayer.getDuelInvite() != null) {
            return duelPlayer.getDuelInvite().getPlayerName();
        } else {
            return null;
        }
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

    /**
     * Handle duel challenging
     *
     * @param duelPlayer the {@link DuelPlayer} object of the challenger
     * @param duelTarget the {@link DuelPlayer} object of the challenged player
     */
    public void challenge(DuelPlayer duelPlayer, DuelPlayer duelTarget) {
        Player player = duelPlayer.getPlayer();
        String name = player.getName();

        if (getDuelInvite(duelTarget) != null && getDuelInvite(duelTarget).equals(name)) {
            player.sendMessage(LocaleLoader.getString("Duel.Challenge.Already_Send", name));
            return;
        }

        Player target = duelTarget.getPlayer();

        player.sendMessage(LocaleLoader.getString("Duel.Challenge.Send", target.getName()));
        duelTarget.setDuelInvitationKey(new DuelInvitationKey(name, Misc.getSystemTime()));

        target.sendMessage(LocaleLoader.getString("Duel.Challenge.Receive.1", name));

        if (Config.getInstance().getChallengeInteractEnabled()) {
            target.sendMessage(LocaleLoader.getString("Duel.Challenge.Receive.2", name));
        }
        else if (Config.getInstance().getChallengeCommandsEnabled()) {
            target.sendMessage(LocaleLoader.getString("Duel.Challenge.Receive.3", name));
        }
    }

    public void removeDuelInvitation(DuelPlayer duelPlayer) {
        duelPlayer.setDuelInvitationKey(null);
    }

    public static List<Player> getDuelingPlayers() {
        Iterable<DuelPlayer> duelPlayers = UserManager.getPlayers();
        List<Player> duelingPlayers = new ArrayList<Player>();

        for (DuelPlayer duelPlayer : duelPlayers) {
            if (duelPlayer.getTargetName() != null) {
                duelingPlayers.add(duelPlayer.getPlayer());
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
