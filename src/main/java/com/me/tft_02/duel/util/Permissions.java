package com.me.tft_02.duel.util;

import org.bukkit.permissions.Permissible;

public class Permissions {

    public static boolean reload(Permissible permissible) {
        return permissible.hasPermission("duel.commands.reload");
    }

    public static boolean stats(Permissible permissible) {
        return permissible.hasPermission("duel.commands.stats");
    }
}
