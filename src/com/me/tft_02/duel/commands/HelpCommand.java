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
                sender.sendMessage(dot + ChatColor.GRAY + "Duel allows players to challenge eachother for a battle.");
                sender.sendMessage(dot + ChatColor.GRAY + "Right-click another player while holding a sword to challenge him.");
                sender.sendMessage(dot + ChatColor.GRAY + "The challengee wil have to right-click the challenger to accept the duel.");

            case 2:
                sender.sendMessage(ChatColor.GOLD + "Commands:");
                if (sender.hasPermission("duel.commands.reload")) {
                    sender.sendMessage(dot + ChatColor.GREEN + "/duel <reload>" + ChatColor.GRAY + " Reload the configuration file");
                }
                if (sender.hasPermission("duel.commands.stats")) {
                    sender.sendMessage(dot + ChatColor.GREEN + "/duel <stats>" + ChatColor.GRAY + " Checkout your stats");
                }
                sender.sendMessage(dot + ChatColor.GREEN + "/duel <help>" + ChatColor.GRAY + " View the help pages");
            default:
                if (nextPage <= maxPages) {
                    sender.sendMessage(ChatColor.GOLD + "Type /duel help " + nextPage + " for more");
                }
        }
    }
}
