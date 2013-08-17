package com.me.tft_02.duel.util.player;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.database.DatabaseManager;
import com.me.tft_02.duel.datatypes.DuelResultType;
import com.me.tft_02.duel.datatypes.player.DuelPlayer;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.events.player.DuelEndEvent;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.runnables.duels.CountdownTask;
import com.me.tft_02.duel.runnables.duels.DuelCommenceTask;
import com.me.tft_02.duel.runnables.duels.DuelEndTask;
import com.me.tft_02.duel.runnables.duels.HealPlayerTask;
import com.me.tft_02.duel.util.ItemUtils;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.Permissions;
import com.me.tft_02.duel.util.RegionUtils;

public class DuelManager {
    private static int MESSAGE_RANGE = Config.getInstance().getMessageRange();

    public enum DuelMessageType {
        START,
        END,
        TIE;
    };

    public static boolean canDuel(Player player) {
        if (!Permissions.challenge(player)) {
            return false;
        }

        if (!player.isSneaking()) {
            return false;
        }

        if (!ItemUtils.isDuelWeapon(player.getItemInHand())) {
            return false;
        }

        if (Duel.p.worldGuardEnabled && !RegionUtils.canDuelHere(player.getLocation())) {
            return false;
        }

        if (UserManager.getPlayer(player).getOccupied()) {
            return false;
        }

        return true;
    }

    public static void notifyPlayers(Location location, DuelMessageType messageType) {
        String message = "";
        switch (messageType) {
            case START:
                message = LocaleLoader.getString("Duel.Started");
                break;
            case END:
                message = LocaleLoader.getString("Duel.Ended", "!");
                break;
            case TIE:
                message = LocaleLoader.getString("Duel.Ended", LocaleLoader.getString("Duel.Tie"));
                break;
            default:
                break;
        }

        notifyPlayers(location, message);
    }

    public static void notifyPlayers(Location location, String message) {
        for (Player player : Misc.getNearbyPlayers(location, MESSAGE_RANGE)) {
            player.sendMessage(message);
        }
    }

    public static void handleDuelInvites(Player player, Player target) {
        PlayerData playerData = new PlayerData();

        DuelPlayer duelPlayer = UserManager.getPlayer(player);

        if (duelPlayer.getDuelInvite() != null && duelPlayer.getDuelInvite().getPlayerName().equals(target.getName())) {
            if (playerData.duelInviteIsTimedout(duelPlayer)) {
                player.sendMessage(LocaleLoader.getString("Duel.Invite.Expired"));
                playerData.removeDuelInvite(duelPlayer);
                return;
            }

            player.sendMessage(LocaleLoader.getString("Duel.Invite.Accepted"));
            target.sendMessage(LocaleLoader.getString("Duel.Invite.Accepted"));

            DuelManager.prepareDuel(player, target);
            new CountdownTask(player.getLocation(), 4).runTaskTimer(Duel.p, 0, 1 * 20);
            new DuelCommenceTask(player, target).runTaskLater(Duel.p, 4 * 20);
        }
        else {
            DuelPlayer duelTarget = UserManager.getPlayer(target);
            playerData.setDuelInvite(duelPlayer, duelTarget);
        }
    }

    public static void prepareDuel(Player player) {
        DuelPlayer duelPlayer = UserManager.getPlayer(player);
        PlayerData playerData = new PlayerData();
        playerData.removeDuelInvitation(duelPlayer);
        duelPlayer.setOccupied(true);
        ArenaManager.setArena(player);
    }

    public static void prepareDuel(Player player, Player target) {
        prepareDuel(player);
        prepareDuel(target);
    }

    public static void startDuel(Player player, Player target) {
        if (!(player.isOnline() && target.isOnline())) {
            return;
        }

        PlayerData playerData = new PlayerData();
        playerData.removeDuelInvite(UserManager.getPlayer(player));
        playerData.removeDuelInvite(UserManager.getPlayer(target));
        playerData.setDuel(player, target);
        notifyPlayers(player.getLocation(), DuelMessageType.START);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);

        int duelLength = Config.getInstance().getDuelLength();
        new DuelEndTask(player).runTaskLater(Duel.p, duelLength * 20);
    }

    public static void endDuelResult(Player winner, Player loser) {
        endDuel(winner, true);
        endDuel(loser, false);

        DuelEndEvent eventWinner = new DuelEndEvent(winner, DuelResultType.WIN);
        Duel.p.getServer().getPluginManager().callEvent(eventWinner);

        DuelEndEvent eventLoser = new DuelEndEvent(loser, DuelResultType.LOSS);
        Duel.p.getServer().getPluginManager().callEvent(eventLoser);

        notifyPlayers(winner.getLocation(), DuelMessageType.END);

        DatabaseManager.increaseWinCount(winner, 1);
        DatabaseManager.increaseLossCount(loser, 1);

        loser.playSound(loser.getLocation(), Sound.WITHER_SPAWN, 1F, 0F);
        winner.playSound(loser.getLocation(), Sound.WITHER_SPAWN, 1F, 0F);

        if (Config.getInstance().getHealEnabled()) {
            new HealPlayerTask(winner).runTask(Duel.p);
        }
    }

    public static void endDuelInTie(Player player) {
        Player target = PlayerData.getDuelTarget(player);

        if (target == null) {
            return;
        }

        DuelEndEvent event = new DuelEndEvent(player, DuelResultType.TIE);
        Duel.p.getServer().getPluginManager().callEvent(event);

        endDuel(player, true);
        endDuel(target, true);

        notifyPlayers(player.getLocation(), DuelMessageType.TIE);

        DatabaseManager.increaseTieCount(player, 1);
        DatabaseManager.increaseTieCount(target, 1);


        if (Config.getInstance().getHealEnabled()) {
            new HealPlayerTask(player).runTask(Duel.p);
            new HealPlayerTask(target).runTask(Duel.p);
        }
    }

    public static void endDuel(Player player, boolean deleteArena) {
        PlayerData.removeDuelTarget(player);
        UserManager.getPlayer(player).setOccupied(false);

        if (deleteArena) {
            ArenaManager.deleteArena(player);
        }
    }
}
