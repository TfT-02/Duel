package com.me.tft_02.duel.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.me.tft_02.duel.Duel;

public class DuelCommand implements CommandExecutor {
    private CommandExecutor reloadCommand = new ReloadCommand();
    private CommandExecutor helpCommand = new HelpCommand();
    private CommandExecutor statsCommand = new StatsCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("duel")) {
            switch (args.length) {
                case 1:
                    if (args[0].equalsIgnoreCase("help")) {
                        return helpCommand.onCommand(sender, command, label, args);
                    }
                    else if (args[0].equalsIgnoreCase("reload")) {
                        return reloadCommand.onCommand(sender, command, label, args);
                    }
                    else if (args[0].equalsIgnoreCase("stats")) {
                        return statsCommand.onCommand(sender, command, label, args);
                    }
                default:
                    return printUsage(sender);
            }
        }
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.GOLD + "Duel" + ChatColor.GRAY + " ]----- by " + ChatColor.GOLD + "TfT_02");

        String version = Duel.getInstance().getDescription().getVersion();
        sender.sendMessage(ChatColor.GRAY + "Running version: " + ChatColor.DARK_AQUA + version);
        sender.sendMessage(ChatColor.GRAY + "Use: /duel help for more information.");
        return true;
    }
}
