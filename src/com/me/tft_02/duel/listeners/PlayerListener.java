package com.me.tft_02.duel.listeners;

import org.bukkit.event.Listener;

import com.me.tft_02.duel.Duel;

public class PlayerListener implements Listener {
    Duel plugin;

    public PlayerListener(final Duel instance) {
        plugin = instance;
    }
}
