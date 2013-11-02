package com.me.tft_02.duel.hooks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.datatypes.player.PlayerData;

import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;

public class WorldGuardListener implements Listener {

    /**
     * Handle DisallowedPVPEvent events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDisallowedPVP(DisallowedPVPEvent event) {
        Player attacker = event.getAttacker();
        Player defender = event.getDefender();

        if (Config.getInstance().getOverridePVP() && PlayerData.areDueling(attacker, defender)) {
            event.setCancelled(true);
        }
    }
}
