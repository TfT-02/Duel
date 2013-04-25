package com.me.tft_02.duel;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    
    static FileConfiguration config = Duel.getInstance().getConfig();

    public static boolean getBorderKnockBack() {
        return true;
//        return config.getBoolean("Duel.Knockback_Enabled");
    }

    public static boolean getPreventPVP() {
        // TODO Auto-generated method stub
        return true;
    }

    public static boolean getOverridePVP() {
        // TODO Auto-generated method stub
        return true;
    }
}
