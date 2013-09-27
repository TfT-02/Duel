package com.me.tft_02.duel.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.config.Config;
import com.me.tft_02.duel.locale.LocaleLoader;

public class DuelCommand implements CommandExecutor {
    private CommandExecutor reloadCommand = new ReloadCommand();
    private CommandExecutor helpCommand = new HelpCommand();
    private CommandExecutor statsCommand = new StatsCommand();
    private CommandExecutor challengeCommand = new ChallengeCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("duel")) {
            switch (args.length) {
                case 1:
                case 2:
                    if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                        return helpCommand.onCommand(sender, command, label, args);
                    }
                    else if (args[0].equalsIgnoreCase("reload")) {
                        return reloadCommand.onCommand(sender, command, label, args);
                    }
                    else if (args[0].equalsIgnoreCase("stats")) {
                        return statsCommand.onCommand(sender, command, label, args);
                    }

                    if (Config.getInstance().getChallengeCommandsEnabled() && (args[0].equalsIgnoreCase("challenge") || args[0].equalsIgnoreCase("request"))) {
                        return challengeCommand.onCommand(sender, command, label, args);
                    }
                default:
                    return printUsage(sender);
            }
        }
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage(LocaleLoader.getString("General.Plugin_Header", Duel.p.getDescription().getName(), Duel.p.getDescription().getAuthors()));
        sender.sendMessage(LocaleLoader.getString("General.Running_Version", Duel.p.getDescription().getVersion()));
        sender.sendMessage(LocaleLoader.getString("General.Use_Help"));
        return true;
    }
}
