package com.me.tft_02.duel.runnables.duels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.me.tft_02.duel.Config;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.locale.LocaleLoader;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.player.ArenaManager;
import com.me.tft_02.duel.util.player.DuelManager;

public class DuelRangeTask extends BukkitRunnable {

    private HashMap<String, Integer> outOfArenaStrikes = new HashMap<String, Integer>();

    @Override
    public void run() {
        checkDuelingPlayers();
    }

    private void checkDuelingPlayers() {
        for (Player player : PlayerData.getDuelingPlayers()) {
            Location playerLocation = player.getLocation();
            Location arenaCenter = ArenaManager.getArenaLocation(player);
            double arenaSize = Config.getArenaSize();

            if (Config.getBorderParticleEffectsEnabled()) {
                drawArenaCircle(arenaCenter, (int) arenaSize, Effect.MOBSPAWNER_FLAMES);
            }

            if (!Misc.isNear(playerLocation, arenaCenter, arenaSize)) {
                if (!Config.getBorderKnockBack()) {
                    DuelManager.endDuelInTie(player);
                    return;
                }

                if (strikePlayer(player)) {
                    playerLocation.setY(playerLocation.getY() - 8);
                    Vector knockback = Misc.getKnockbackVector(playerLocation, arenaCenter);

                    player.getWorld().playSound(playerLocation, Sound.FIZZ, 1F, 1F);
                    player.setVelocity(knockback.multiply(2));
                    player.sendMessage(LocaleLoader.getString("Duel.Range.Warning"));
                }
                else {
                    Player target = PlayerData.getDuelTarget(player);
                    player.sendMessage(LocaleLoader.getString("Duel.Range.Teleport", player.getName()));
                    target.sendMessage(LocaleLoader.getString("Duel.Range.Teleport", player.getName()));

                    player.teleport(arenaCenter);
                    player.getWorld().playSound(playerLocation, Sound.ENDERMAN_TELEPORT, 1F, 0.5F);
                }
            }
        }
    }

    private boolean strikePlayer(Player player) {
        String playerName = player.getName();
        int strikes = 0;

        if (outOfArenaStrikes.containsKey(playerName)) {
            strikes = outOfArenaStrikes.get(playerName);
            strikes++;
        }

        if (strikes >= 3) {
            outOfArenaStrikes.remove(playerName);
            return false;
        }
        else {
            outOfArenaStrikes.put(playerName, strikes);
            return true;
        }
    }

    private void drawArenaCircle(Location loc, int r, Effect effect) {
        List<Location> circleBlocks = sphere(loc, r, 1, true, false, 0);
        World world = loc.getWorld();

        for (Location location : circleBlocks) {
            world.playEffect(location, effect, 1);
        }
    }

    public static List<Location> sphere(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }

        return circleblocks;
    }
}
