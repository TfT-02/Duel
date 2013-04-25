package com.me.tft_02.duel.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.me.tft_02.duel.Duel;

public class UpdateChecker {
    static Duel plugin;

    private UpdateChecker() {}

    public static boolean updateAvailable() throws Exception {
        String version = Duel.getInstance().getDescription().getVersion();
        URL url = new URL("http://api.bukget.org/api2/bukkit/plugin/duel/latest");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(url.openStream());
        }
        catch (UnknownHostException e) {
            return false;
        }

        String newVersion;
        try {
            JSONParser jp = new JSONParser();
            Object o = jp.parse(isr);

            if (!(o instanceof JSONObject)) {
                isr.close();
                return false;
            }

            JSONObject jo = (JSONObject) o;
            jo = (JSONObject) jo.get("versions");
            newVersion = (String) jo.get("version");

            String[] oldTokens = version.split("[.]");
            String[] newTokens = newVersion.split("[.]");

            for (int i = 0; i < 3; i++) {
                Integer newVer = Integer.parseInt(newTokens[i]);
                Integer oldVer;
                try {
                    oldVer = Integer.parseInt(oldTokens[i]);
                }
                catch (NumberFormatException e) {
                    oldVer = 0;
                }
                if (oldVer < newVer) {
                    isr.close();
                    return true;
                }
            }
            return false;
        }
        catch (ParseException e) {
            isr.close();
            return false;
        }
    }

    public static String updateStatus() {
        String status;

        if (Duel.getInstance().updateAvailable) {
            status = ChatColor.DARK_RED + "OUTDATED";
        }
        else {
            status = ChatColor.GREEN + "UP TO DATE";
        }

        return status;
    }
}
