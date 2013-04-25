package com.me.tft_02.duel.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.database.DatabaseManager;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("sorry!");
        }

        Player player = (Player) sender;

        player.sendMessage("STATS");

        player.sendMessage("WINS: " + DatabaseManager.getWinCount(player));
        player.sendMessage("LOSSES: " + DatabaseManager.getLossCount(player));
        player.sendMessage("TIES: " + DatabaseManager.getTieCount(player));
        return true;
    }
}
