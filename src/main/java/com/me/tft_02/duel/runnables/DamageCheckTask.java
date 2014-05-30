package com.me.tft_02.duel.runnables;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.duel.listeners.EntityListener;

public class DamageCheckTask extends BukkitRunnable {
    private EntityDamageEvent event;
    private Player player;
    private double baseHealth;

    public DamageCheckTask(EntityDamageEvent event, Player player) {
        this.event = event;
        this.player = player;
        baseHealth = player.getHealth();
    }

    @Override
    public void run() {
        double health = player.getHealth();
        double damage = baseHealth - health;

        // May avoid negative damage, we don't know what other plugins do with the entity health
        if (damage <= 0) {
            return;
        }

        // Don't reward the player for overkills
        if (health < 0) {
            damage += health;
        }

        EntityListener.handleDamageEvent(event, player, damage);
    }
}
