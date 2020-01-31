package com.neo.community.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Utils {
	public static long getStartOfCurrentDay() {
		ZonedDateTime startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		return startOfToday.toEpochSecond();
	}
	
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getPlayerFromUsername(String name) {
		OfflinePlayer result = Bukkit.getPlayerExact(name);
		if(result == null)
			result = Bukkit.getPlayer(name);
		if(result == null)
			result = Bukkit.getOfflinePlayer(name);
		if(result.hasPlayedBefore())
			return result;
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}
}
