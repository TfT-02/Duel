package com.me.tft_02.duel.listeners;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.datatypes.player.PlayerData;
import com.me.tft_02.duel.util.Misc;

public class EntityListener implements Listener {

    /**
     * Handle EntityDamageByEntityEvent events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamage() <= 0) {
            return;
        }

        Entity attacker = event.getDamager();
        Entity defender = event.getEntity();

        if (Misc.isNPCEntity(attacker) || Misc.isNPCEntity(defender)) {
            return;
        }

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
                Player attackingPlayer = (Player) attacker;
                if (Config.getInstance().getOverridePVP() && PlayerData.areDueling(attackingPlayer, defendingPlayer)) {
                    event.setCancelled(false);
                }
            }
        }
    }

    /**
     * Handle EntityDamageByEntityEvent events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityLowest(EntityDamageByEntityEvent event) {
        if (event.getDamage() <= 0) {
            return;
        }

        Entity attacker = event.getDamager();
        Entity defender = event.getEntity();

        if (Misc.isNPCEntity(attacker) || Misc.isNPCEntity(defender)) {
            return;
        }

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
                Player attackingPlayer = (Player) attacker;
                if (Config.getInstance().getPreventPVP() && !PlayerData.areDueling(attackingPlayer, defendingPlayer)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Handle PotionSplashEvent events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        LivingEntity shooter = event.getPotion().getShooter();

        if (!(shooter instanceof Player)) {
            return;
        }

        Player player = (Player) shooter;

        if (Misc.isNPCEntity(player)) {
            return;
        }

        for (LivingEntity entity : event.getAffectedEntities()) {
            if (entity instanceof Player) {
                Player target = (Player) entity;

                if (player == target) {
                    continue;
                }

                if (PlayerData.isInDuel(target)) {
                    if (!PlayerData.areDueling(player, target)) {
                        event.setIntensity(target, 0);
                    }
                }
                else if (Config.getInstance().getPreventPVP()) {
                    event.setIntensity(target, 0);
                }
            }
        }
    }
}
