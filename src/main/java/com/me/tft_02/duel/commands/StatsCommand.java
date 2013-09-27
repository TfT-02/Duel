package com.me.tft_02.duel.commands;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.me.tft_02.duel.database.DatabaseManager;
import com.me.tft_02.duel.util.CommandUtils;

public class StatsCommand implements CommandExecutor {

    protected DecimalFormat decimal = new DecimalFormat("##0.00");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (CommandUtils.noConsoleUsage(sender)) {
            return true;
        }

        Player player = (Player) sender;
        int wins = DatabaseManager.getWinCount(player);
        int losses = DatabaseManager.getLossCount(player);
        int ties = DatabaseManager.getTieCount(player);
        String ratio = calculateRatio(wins, losses);

        String border = ChatColor.GOLD + "|";

        player.sendMessage(border + ChatColor.GOLD + "----[ " + ChatColor.DARK_RED + "Duel Stats" + ChatColor.GOLD + " ]----- ");
        player.sendMessage(border + ChatColor.DARK_AQUA + "  WINS: " + ChatColor.GRAY + wins);
        player.sendMessage(border + ChatColor.DARK_AQUA + "  LOSSES: " + ChatColor.GRAY + losses);
        player.sendMessage(border + ChatColor.DARK_AQUA + "  TIES: " + ChatColor.GRAY + ties);
        player.sendMessage(border + ChatColor.GOLD + "--------------------");
        player.sendMessage(border + ChatColor.DARK_AQUA + "  W/L RATIO: " + ChatColor.GRAY + ratio);
        player.sendMessage(border + ChatColor.GOLD + "--------------------");
        return true;
    }

    private String calculateRatio(int first, int second) {
        double ratio = 0.0;
        if (second > 0) {
            ratio = first / second;
        }

        return decimal.format(ratio);
    }
}
