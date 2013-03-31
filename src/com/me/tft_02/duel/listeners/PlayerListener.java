package com.me.tft_02.duel.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.util.ItemUtils;
import com.me.tft_02.duel.util.PlayerData;

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

                if (playerData.isInDuel(target)) {
                    player.sendMessage("Player is occupied");
                }
                    
                if (playerData.getDuelInvite(target).equals(player.getName())) {
                    player.sendMessage("This is where normally a duel would start! :D");
                } else {
                    playerData.setDuelInvite(player, target);
                }
                //                new DuelCommenceTask(player).runTaskLater(Duel.getInstance(), 2);
            }
        }
    }
}
