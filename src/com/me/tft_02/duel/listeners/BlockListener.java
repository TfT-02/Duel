package com.me.tft_02.duel.listeners;

import org.bukkit.event.Listener;

import com.me.tft_02.duel.Duel;

public class BlockListener implements Listener {
    Duel plugin;

    public BlockListener(final Duel instance) {
        plugin = instance;
    }

}