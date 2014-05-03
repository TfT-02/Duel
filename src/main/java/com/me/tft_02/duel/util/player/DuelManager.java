package com.me.tft_02.duel.util.player;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.me.tft_02.assassin.api.AssassinAPI;
import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.database.DatabaseManager;
import com.me.tft_02.duel.datatypes.DuelInvitationKey;
import com.me.tft_02.duel.datatypes.DuelResultType;
import com.me.tft_02.duel.datatypes.player.DuelPlayer;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.events.player.DuelEndEvent;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.runnables.duels.CountdownTask;
import com.me.tft_02.duel.runnables.duels.DuelEndTask;
import com.me.tft_02.duel.runnables.player.HealPlayerTask;
import com.me.tft_02.duel.util.ItemUtils;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.Permissions;
import com.me.tft_02.duel.util.RegionUtils;

public class DuelManager {

    public static boolean canDuel(Player player) {
        return canDuel(player, true);
    }

    public static boolean canDuel(Player player, boolean interact) {
        if (!Permissions.challenge(player)) {
            return false;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }

        DuelPlayer duelPlayer = UserManager.getPlayer(player);

        if (interact) {
            if (!player.isSneaking()) {
                return false;
            }

            if (!ItemUtils.isDuelWeapon(player.getItemInHand())) {
                return false;
            }

            if (!Misc.cooldownExpired(duelPlayer.getLastChallengeSend(), 3)) {
                return false;
            }
        }

        if (!RegionUtils.canDuelHere(player.getWorld())) {
            player.sendMessage(LocaleLoader.getString("Duel.Challenge.World"));
            return false;
        }

        if (!RegionUtils.canDuelHereWG(player.getLocation())) {
            player.sendMessage(LocaleLoader.getString("Duel.Challenge.WorldGuard"));
            return false;
        }

        if (Duel.p.isAssassinEnabled() && AssassinAPI.isAssassin(player)) {
            return false;
        }

        if (PlayerData.isInDuel(player)) {
            return false;
        }

        return true;
    }

    public static void notifyNearbyPlayers(Location location, String message) {
        for (Player player : Misc.getNearbyPlayers(location, Config.getInstance().getMessageRange())) {
            player.sendMessage(message);
        }
    }

    public static void handleDuelInvites(Player player, Player target) {
        PlayerData playerData = new PlayerData();

        DuelPlayer duelPlayer = UserManager.getPlayer(player);
        DuelInvitationKey duelInvite = duelPlayer.getDuelInvite();

        if (!acceptChallenge(target, duelInvite)) {
            DuelPlayer duelTarget = UserManager.getPlayer(target);
            playerData.challenge(duelPlayer, duelTarget);
            return;
        }

        if (playerData.duelInviteIsTimedout(duelPlayer)) {
            player.sendMessage(LocaleLoader.getString("Duel.Challenge.Expired"));
            playerData.removeDuelInvite(duelPlayer);
            return;
        }

        Location middle = Misc.getMiddle(player.getLocation(), target.getLocation());
        notifyNearbyPlayers(middle, LocaleLoader.getString("Duel.Challenge.Accepted", player.getName(), target.getName()));

        DuelManager.prepareDuel(player, target);

        new CountdownTask(player, target, 3).runTaskTimer(Duel.p, 0, Misc.TICK_CONVERSION_FACTOR);
    }

    private static boolean acceptChallenge(Player target, DuelInvitationKey duelInvite) {
        return duelInvite != null && duelInvite.getPlayerName().equals(target.getName());
    }

    public static void prepareDuel(Player player) {
        DuelPlayer duelPlayer = UserManager.getPlayer(player);
        PlayerData playerData = new PlayerData();
        playerData.removeDuelInvitation(duelPlayer);
        duelPlayer.setOccupied(true);
    }

    public static void prepareDuel(Player player, Player target) {
        prepareDuel(player);
        prepareDuel(target);

        ArenaManager.setArena(player, target);
    }

    public static void startDuel(Player player, Player target) {
        if (!(player.isOnline() && target.isOnline())) {
            return;
        }

        PlayerData playerData = new PlayerData();
        DuelPlayer duelPlayer = UserManager.getPlayer(player);
        DuelPlayer duelTarget = UserManager.getPlayer(target);

        playerData.removeDuelInvite(duelPlayer);
        playerData.removeDuelInvite(duelTarget);
        playerData.setDuel(player, target);

        player.sendMessage(LocaleLoader.getString("Duel.Started"));
        target.sendMessage(LocaleLoader.getString("Duel.Started"));

        removePotionEffects(player);
        removePotionEffects(target);

        player.getLocation().getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);

        int duelLength = Config.getInstance().getDuelLength();

        duelPlayer.setDuelEndTask(new DuelEndTask(player).runTaskLater(Duel.p, duelLength * Misc.TICK_CONVERSION_FACTOR));
        duelTarget.setDuelEndTask(new DuelEndTask(target).runTaskLater(Duel.p, duelLength * Misc.TICK_CONVERSION_FACTOR));
    }

    public static void endDuelResult(Player winner, Player loser) {
        endDuel(winner, true);
        endDuel(loser, false);

        DuelEndEvent eventWinner = new DuelEndEvent(winner, DuelResultType.WIN);
        Duel.p.getServer().getPluginManager().callEvent(eventWinner);

        DuelEndEvent eventLoser = new DuelEndEvent(loser, DuelResultType.LOSS);
        Duel.p.getServer().getPluginManager().callEvent(eventLoser);

        notifyNearbyPlayers(winner.getLocation(), LocaleLoader.getString("Duel.End.Result", winner.getName(), loser.getName()));

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

        notifyNearbyPlayers(player.getLocation(), LocaleLoader.getString("Duel.End.Tie", player.getName(), target.getName()));

        DatabaseManager.increaseTieCount(player, 1);
        DatabaseManager.increaseTieCount(target, 1);


        if (Config.getInstance().getHealEnabled()) {
            new HealPlayerTask(player).runTask(Duel.p);
            new HealPlayerTask(target).runTask(Duel.p);
        }
    }

    public static void endDuel(Player player, boolean deleteArena) {
        DuelPlayer duelPlayer = UserManager.getPlayer(player);

        if (duelPlayer.getDuelEndTask() != null) {
            duelPlayer.getDuelEndTask().cancel();
            duelPlayer.setDuelEndTask(null);
        }

        PlayerData.removeDuelTarget(player);
        duelPlayer.setOccupied(false);

        if (deleteArena) {
            ArenaManager.deleteArena(player);
        }
    }

    private static void removePotionEffects(Player player) {
        List<String> effectsToRemove = Config.getInstance().getPotionEffectsToRemove();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effectsToRemove.contains(effect.toString())) {
                player.removePotionEffect(effect.getType());
            }
        }
    }
}
