package com.me.tft_02.duel.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                if (Integer.parseInt(args[1]) > 1) {
                    getHelpPage(Integer.parseInt(args[1]), sender);
                    return true;
                }
                else {
                    getHelpPage(1, sender);
                    return true;
                }

            default:
                getHelpPage(1, sender);
                return true;
        }
    }

    private void getHelpPage(int page, CommandSender sender) {
        int maxPages = 2;
        int nextPage = page + 1;

        if (page > maxPages) {
            sender.sendMessage(ChatColor.RED + "This page does not exist." + ChatColor.GOLD + " /help [0-" + maxPages + "]");
            return;
        }

        String dot = ChatColor.DARK_RED + "* ";
        sender.sendMessage(ChatColor.GOLD + "-----[ " + ChatColor.DARK_RED + "Duel Help" + ChatColor.GOLD + " ]----- Page " + page + "/" + maxPages);

        switch (page) {
            case 1:
                sender.sendMessage(ChatColor.GOLD + "How does it work?");
                sender.sendMessage(dot + ChatColor.GRAY + "When in Assassin mode, you can PVP.");
                sender.sendMessage(dot + ChatColor.GRAY + "Your skin and name will be hidden, even in chat.");
                sender.sendMessage(dot + ChatColor.GRAY + "You can chat with other Assassins in 'Assassin Chat'.");
                sender.sendMessage(dot + ChatColor.GRAY + "When the timer expires, you will be teleported back to where you put on your mask.");
                sender.sendMessage(dot + ChatColor.GRAY + "Nobody will ever know that you were an Assassin.");

            case 2:
                sender.sendMessage(ChatColor.GOLD + "Commands:");
                if (sender.hasPermission("duel.commands")) {
                    sender.sendMessage(dot + ChatColor.GREEN + "/duel [reload]/[help]" + ChatColor.GRAY + " General commands");
                }
            default:
                if (nextPage <= maxPages) {
                    sender.sendMessage(ChatColor.GOLD + "Type /duel help " + nextPage + " for more");
                }
        }
    }
}
