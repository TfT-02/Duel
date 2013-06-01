package com.me.tft_02.duel.hooks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.me.tft_02.duel.Config;
import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;

public class WorldGuardListener implements Listener {
    Duel plugin;

    public WorldGuardListener(final Duel instance) {
        plugin = instance;
    }

    /**
     * Handle DisallowedPVPEvent events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisallowedPVP(DisallowedPVPEvent event) {
        Player attacker = event.getAttacker();
        Player defender = event.getDefender();

        if (Config.getOverridePVP() && PlayerData.areDueling(attacker, defender)) {
            event.setCancelled(true);
        }
    }
}
