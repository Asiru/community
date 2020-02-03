package com.neo.community.command;

import com.neo.community.Community;
import com.neo.community.config.database.ScoreEntry;
import com.neo.community.manager.CooldownManager;
import com.neo.community.util.Message;
import com.neo.community.util.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class RewardExecutor extends CooldownManager<OfflinePlayer> implements CommandExecutor {
	private Community plugin;
	
	public RewardExecutor(Community plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// check permission
		if(!sender.hasPermission("community.reward")) {
			Message.NO_PERMISSION.send(sender, "/reward <player>");
			return false;
		}
		
		if(args.length >= 1) {
			OfflinePlayer target = Utils.getPlayerFromUsername(args[0]);
			if(target == null) {
				Message.PLAYER_INVALID.send(sender, args[0]);
				return false;
			}
			
			if(target.equals(sender)) {
				Message.PLAYER_SELF.send(sender);
				return false;
			}
			
			double points;
			if(args.length >= 2) {
				try {
					points = Double.valueOf(args[1]);
				} catch(NumberFormatException ex) {
					Message.AMOUNT_INVALID.send(sender, args[1]);
					return false;
				}
				
				if(!sender.hasPermission("community.reward.bypass")) {
					Message.NO_PERMISSION.send(sender, "/reward <player> <amount>");
					return false;
				}
			} else {
				Double permissionValue = Utils.getPermissionValue(sender, "community.reward");
				if(permissionValue == null) {
					points = plugin.getSettings().getRewardPoints();
				} else {
					points = permissionValue;
				}
			}
			
			Double permissionValue = Utils.getPermissionValue(sender, "community.reward.cooldown");
			long cooldown;
			if(permissionValue == null) {
				cooldown = plugin.getSettings().getRewardCooldown();
			} else {
				cooldown = Math.round(permissionValue);
			}
			
			if(!sender.hasPermission("community.reward.cooldown.bypass")) {
				if(sender instanceof OfflinePlayer) {
					long current = getCooldown((OfflinePlayer) sender);
					if(current > 0) {
						String formattedTime = formatTime(current);
						Message.REWARD_COOLDOWN.send(sender, formattedTime);
						return false;
					} else {
						setCooldown((OfflinePlayer) sender, cooldown);
					}
				}
			}
			
			ScoreEntry scoreEntry = plugin.getPlayerDataStorage().get(target.getUniqueId().toString());
			scoreEntry.setScore(scoreEntry.getScore() + points);
			if(sender.hasPermission("community.reward.anonymous")) {
				Message.REWARD_ANONYMOUS.send(sender, points);
			} else {
				Message.REWARD_MESSAGE.send(sender, sender.getName(), points);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected void start(BukkitRunnable runnable) {
		runnable.runTaskTimer(plugin, 0L, 20L);
	}
	
	private String formatTime(long seconds) {
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
}
