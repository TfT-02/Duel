package com.me.tft_02.duel.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.runnables.CountdownTask;
import com.me.tft_02.duel.runnables.DuelCommenceTask;
import com.me.tft_02.duel.util.ItemUtils;
import com.me.tft_02.duel.util.PlayerData;
import com.me.tft_02.duel.util.player.DuelManager;

public class PlayerListener implements Listener {
    Duel plugin;

    public PlayerListener(final Duel instance) {
        plugin = instance;
    }

    PlayerData playerData = new PlayerData();

    /**
     * Monitor PlayerInteractEntityEvent events.
     * 
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        ItemStack inHand = player.getItemInHand();

        if (entity instanceof Player) {
            Player target = (Player) entity;

            if (ItemUtils.isSword(inHand)) {

                if (PlayerData.areDueling(player, target)) {
                    player.sendMessage("[Debug] Dualing..");
                    return;
                }

                event.setCancelled(true);
                if (playerData.getDuelInvite(player).equals(target.getName())) {
                    DuelManager.prepareDuel(player, target);
                    new CountdownTask(player.getLocation(), 4).runTaskTimer(Duel.getInstance(), 0, 1 * 20);
                    new DuelCommenceTask(player, target).runTaskLater(Duel.getInstance(), 5 * 20);
                }
                else {
                    playerData.setDuelInvite(player, target);
                }
            }
        }
    }

    /**
     * Check PlayerTeleportEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void ongPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (PlayerData.isInDuel(player)) {
            event.setCancelled(true);
        }
    }

    /**
     * Check PlayerTeleportEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (PlayerData.isInDuel(player)) {
            Player target = PlayerData.getDuelTarget(player);

            DuelManager.endDuel(target, player);
        }
    }
}
