package com.me.tft_02.duel.commands;

public enum DuelSubcommandType {
    RELOAD,
    HELP,
    STATS,
    CHALLENGE;

    public static DuelSubcommandType getSubcommand(String commandName) {
        for (DuelSubcommandType command : values()) {
            if (command.name().equalsIgnoreCase(commandName)) {
                return command;
            }
        }

        if (commandName.equalsIgnoreCase("?")) {
            return HELP;
        }
        else if (commandName.equalsIgnoreCase("r")) {
            return RELOAD;
        }
        else if (commandName.equalsIgnoreCase("request")) {
            return CHALLENGE;
        }

        return null;
    }
}
