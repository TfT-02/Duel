package com.me.tft_02.duel.listeners;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.util.PlayerData;

public class EntityListener implements Listener {
    Duel plugin;

    public EntityListener(final Duel instance) {
        plugin = instance;
    }

    PlayerData playerData = new PlayerData();

    /**
     * Handle EntityDamageByEntityEvent events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamage() <= 0)
            return;

        Entity attacker = event.getDamager();
        Entity defender = event.getEntity();

        if (attacker.hasMetadata("NPC") || defender.hasMetadata("NPC"))
            return; // Check if either players is are Citizens NPCs

        if (attacker instanceof Projectile) {
            attacker = ((Projectile) attacker).getShooter();
        }
        else if (attacker instanceof Tameable) {
            AnimalTamer animalTamer = ((Tameable) attacker).getOwner();

            if (animalTamer instanceof Entity) {
                attacker = (Entity) animalTamer;
            }
        }

        if (defender instanceof Player) {
            Player defendingPlayer = (Player) defender;

            if (!defendingPlayer.isOnline()) {
                return;
            }

            if (attacker instanceof Player) {
                if (!PlayerData.areDueling((Player) attacker, (Player) defender)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}