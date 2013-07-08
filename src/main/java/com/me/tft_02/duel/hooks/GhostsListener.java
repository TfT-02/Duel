package com.me.tft_02.duel.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.ghosts.events.tomb.TombCreateEvent;

public class GhostsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreateTomb(TombCreateEvent event) {
        if (PlayerData.isInDuel(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
