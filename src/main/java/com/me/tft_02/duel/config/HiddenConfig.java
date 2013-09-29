package com.me.tft_02.duel.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.me.tft_02.duel.Duel;

public class HiddenConfig {
    private static final Duel plugin = Duel.p;
    private static HiddenConfig instance;
    private final File configFile;

    private boolean commandEnabled = true;

    private HiddenConfig() {
        configFile = new File(plugin.getDataFolder(), "hidden.yml");
        load();
    }

    public static HiddenConfig getInstance() {
        if (instance == null) {
            instance = new HiddenConfig();
        }

        return instance;
    }

    private void load() {
        if (!configFile.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        commandEnabled = config.getBoolean("Options.Duel_Command_Enabled", true);
    }

    public boolean isCommandEnabled() {
        return commandEnabled;
    }
}
