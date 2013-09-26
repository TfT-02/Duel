package com.me.tft_02.duel.util;

import org.bukkit.permissions.Permissible;

public class Permissions {

    public static boolean challenge(Permissible permissible) {
        return permissible.hasPermission("duel.challenge");
    }

    public static boolean updateNotifications(Permissible permissible) {
        return permissible.hasPermission("duel.updatecheck");
    }

    public static boolean reload(Permissible permissible) {
        return permissible.hasPermission("duel.commands.reload");
    }

    public static boolean stats(Permissible permissible) {
        return permissible.hasPermission("duel.commands.stats");
    }
}
