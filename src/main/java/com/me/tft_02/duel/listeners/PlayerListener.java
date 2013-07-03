package com.me.tft_02.duel.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.duel.Config;
import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.runnables.RetrieveLevelsTask;
import com.me.tft_02.duel.util.Misc;
import com.me.tft_02.duel.util.player.ArenaManager;
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
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof Player) {
            Player target = (Player) entity;

            if (DuelManager.canDuel(player)) {
                if (PlayerData.isInDuel(player) || PlayerData.isInDuel(target)) {
                    return;
                }

                event.setCancelled(true);

                DuelManager.handleDuelInvites(player, target);
            }
        }
    }

    /**
     * Check PlayerTeleportEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location targetLocation = event.getTo();
        Location arenaCenter = ArenaManager.getArenaLocation(player);

        if (!PlayerData.isInDuel(player)) {
            return;
        }

        if (!Misc.isNear(targetLocation, arenaCenter, Config.getArenaSize())) {
            event.setCancelled(true);
        }
    }

    /**
     * Check PlayerQuitEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (PlayerData.isInDuel(player)) {
            Player target = PlayerData.getDuelTarget(player);

            DuelManager.endDuelResult(target, player);
        }
    }

    /**
     * Check PlayerRespawnEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location arenaCenter = ArenaManager.getArenaLocation(player);

        if (PlayerData.wasInDuel(player)) {
            event.setRespawnLocation(arenaCenter);
            PlayerData.duelRespawn.put(player.getName(), false);

            if (Config.getSaveInventory()) {
                List<ItemStack> armorList = PlayerData.retrieveArmor(player);
                ItemStack[] armor = armorList.toArray(new ItemStack[armorList.size()]);
                player.getInventory().setArmorContents(armor);

                List<ItemStack> items = new ArrayList<ItemStack>();
                items = PlayerData.retrieveInventory(player);
                if (items != null) {
                    for (ItemStack item : items) {
                        player.getInventory().addItem(item);
                    }
                }
                new RetrieveLevelsTask(player).runTaskLater(Duel.getInstance(), 1);

                player.updateInventory();
            }
        }
    }

    /**
     * Check PlayerDeathEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!PlayerData.isInDuel(player)) {
            return;
        }

        PlayerData.duelRespawn.put(player.getName(), true);

        if (Config.getSaveInventory()) {
            List<ItemStack> armorItems = new ArrayList<ItemStack>();
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor != null) {
                    armorItems.add(armor);
                }
            }

            List<ItemStack> items = new ArrayList<ItemStack>();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    items.add(item);
                }
            }

            PlayerData.storeArmor(player, armorItems);
            PlayerData.storeInventory(player, items);
            PlayerData.storeLevelsAndExp(player);

            event.getDrops().clear();
        }

        DuelManager.endDuelResult(PlayerData.getDuelTarget(player), player);
    }
}
