package com.neo.community.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Utils {
	private static final DecimalFormat DF_POINTS = new DecimalFormat("0.##");
	
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
	
	// Generic method for checking permissions regardless of op
	public static Double getPermissionValue(Permissible p, String checkPermission) {
		if(p != null) {
			for (PermissionAttachmentInfo pai : p.getEffectivePermissions()) {
				String regex = "\\Q" + checkPermission + ".\\E";
				String permission = pai.getPermission().toLowerCase();
				if (permission.matches(regex + "\\d+([.]\\d+)?"))
					return Double.valueOf(permission.replaceFirst(regex, ""));
			}
		}
		return null;
	}
	
	public static String formatTime(long seconds) {
		long days = seconds / 86400;
		long hours = seconds / 3600;
		long minutes = seconds / 60;
		long remaining = seconds % 60;
		
		StringBuilder builder = new StringBuilder();
		if(days > 0) {
			builder.append(days);
			builder.append(" day");
			if(days != 1)
				builder.append("s");
		} else if(hours > 0) {
			builder.append(hours);
			builder.append(" hour");
			if(hours != 1)
				builder.append("s");
		} else if(minutes > 0) {
			builder.append(minutes);
			builder.append(" minute");
			if(minutes != 1)
				builder.append("s");
		} else {
			builder.append(remaining);
			builder.append(" second");
			if(remaining != 1)
				builder.append("s");
		}
		
		return builder.toString();
	}
	
	public static String formatPoints(double points) {
		return DF_POINTS.format(points);
	}
}
