package com.me.tft_02.duel.util.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class VanishManager {
	public static List<String> hiddenFromDuelingPlayers = new ArrayList<String>();
	public static List<String> hiddenFromOtherPlayers = new ArrayList<String>();

	public static void hideOthersFromDuelingPlayers(Player player, Player target) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			hiddenFromDuelingPlayers.add(p.getName());
			target.hidePlayer(p);
			player.hidePlayer(p);
		}
	}

	public static void unhideOthersFromDuelingPlayers(Player player, Player target) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			hiddenFromDuelingPlayers.remove(p.getName());
			target.showPlayer(p);
			player.showPlayer(p);
		}
	}

	public static void hideDuelingPlayersFromOthers(Player player, Player target) {
		hiddenFromOtherPlayers.add(player.getName());
		hiddenFromOtherPlayers.add(target.getName());

		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (!p.getName().equalsIgnoreCase(player.getName()) && !p.getName().equalsIgnoreCase(target.getName())) {
				player.hidePlayer(p);
				target.hidePlayer(p);
			}
		}
	}

	public static void unhideDuelingPlayersFromOthers(Player player, Player target) {
		hiddenFromOtherPlayers.remove(player.getName());
		hiddenFromOtherPlayers.remove(target.getName());

		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			player.showPlayer(p);
			target.showPlayer(p);
		}
	}
}
