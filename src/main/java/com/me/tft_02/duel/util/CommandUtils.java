package com.me.tft_02.duel.util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.datatypes.InteractType;
import com.me.tft_02.duel.locale.LocaleLoader;

import com.google.common.collect.ImmutableList;

public final class CommandUtils {
    public static final List<String> TRUE_FALSE_OPTIONS = ImmutableList.of("on", "off", "true", "false", "enabled", "disabled");
    public static final List<String> RESET_OPTIONS = ImmutableList.of("clear", "reset");

    private CommandUtils() {}

    public static boolean noConsoleUsage(CommandSender sender) {
        if (sender instanceof Player) {
            return false;
        }

        sender.sendMessage(LocaleLoader.getString("Commands.NoConsole"));
        return true;
    }

    public static boolean tooFar(CommandSender sender, Player target, int distance) {
        if (sender instanceof Player && !Misc.isNear(((Player) sender).getLocation(), target.getLocation(), distance)) {
            sender.sendMessage(LocaleLoader.getString("Duel.Challenge.Too_Far"));
            return true;
        }

        return false;
    }

    public static boolean isOffline(CommandSender sender, Player player) {
        if (player != null && player.isOnline()) {
            return false;
        }

        sender.sendMessage(LocaleLoader.getString("Commands.Offline"));
        return true;
    }

    public static String getControls(boolean and) {
        return getControlsHoldShift(and) + getControlsRightLeftClick();
    }

    public static String getControlsHoldShift(boolean and) {
        if (!Config.getInstance().getChallengeHoldShift()) {
            return "";
        }
        else {
            if (and) {
                return LocaleLoader.getString("Duel.Challenge.Hold") + " " + LocaleLoader.getString("Duel.Challenge.Shift") + " " + LocaleLoader.getString("Duel.Challenge.And") + ChatColor.DARK_AQUA + " ";
            }
            else {
                return LocaleLoader.getString("Duel.Challenge.Hold") + " " + LocaleLoader.getString("Duel.Challenge.Shift") + " + ";
            }
        }
    }

    public static String getControlsRightLeftClick() {
        if (Config.getInstance().getChallengeInteractType() == InteractType.LEFT_CLICK) {
            return LocaleLoader.getString("Duel.Challenge.LeftClick");
        }
        else {
            return LocaleLoader.getString("Duel.Challenge.RightClick");
        }
    }

    public static boolean shouldEnableToggle(String arg) {
        return arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("enabled");
    }

    public static boolean shouldDisableToggle(String arg) {
        return arg.equalsIgnoreCase("off") || arg.equalsIgnoreCase("false") || arg.equalsIgnoreCase("disabled");
    }
}
