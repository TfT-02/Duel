package com.me.tft_02.duel.util;

import java.util.LinkedList;

import org.bukkit.Location;

import com.me.tft_02.duel.Config;
import com.me.tft_02.duel.Duel;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionUtils {

    public static boolean canDuelHere(Location location) {
        return !isListedRegion(getRegion(location)) && Config.getUseAsWhitelist();
    }

    private static boolean isListedRegion(String region) {
        for (String name : Config.getRegionList()) {
            if (region.equalsIgnoreCase("[" + name + "]")) {
                return true;
            }
        }
        return false;
    }

    public static String getRegion(Location location) {
        RegionManager regionManager = Duel.getInstance().getWorldGuard().getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        LinkedList<String> parentNames = new LinkedList<String>();
        LinkedList<String> regions = new LinkedList<String>();

        for (ProtectedRegion region : set) {
            String id = region.getId();
            regions.add(id);
            ProtectedRegion parent = region.getParent();
            while (parent != null) {
                parentNames.add(parent.getId());
                parent = parent.getParent();
            }
        }

        for (String name : parentNames) {
            regions.remove(name);
        }
        return regions.toString();
    }
}
