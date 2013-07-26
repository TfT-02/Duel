package com.me.tft_02.duel.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.me.tft_02.duel.Duel;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Duel.p.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
        return true;
    }
}
