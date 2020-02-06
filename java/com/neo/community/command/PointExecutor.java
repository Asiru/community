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

public abstract class PointExecutor extends CooldownManager<OfflinePlayer> implements CommandExecutor {
	protected Community plugin;
	private Mode mode;
	
	PointExecutor(Community plugin, Mode mode) {
		this.plugin = plugin;
		this.mode = mode == null ? Mode.REWARD : mode;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("community." + mode.name)) {
			Message.NO_PERMISSION.send(sender, "/" + mode.name + " <player> [amount]");
			return false;
		}
		
		if(args.length == 1 || args.length == 2) {
			OfflinePlayer target = Utils.getPlayerFromUsername(args[0]);
			if(target == null) {
				Message.PLAYER_INVALID.send(sender, args[0]);
				return false;
			}
			
			if(target.equals(sender) && !Community.DEBUG) {
				Message.PLAYER_SELF.send(sender);
				return false;
			}
			
			double points;
			if(args.length == 2) {
				try {
					points = Double.valueOf(args[1]);
				} catch(NumberFormatException ex) {
					Message.AMOUNT_INVALID.send(sender, args[1]);
					return false;
				}
				
				if(!sender.hasPermission("community." + mode.name + ".bypass")) {
					Message.NO_PERMISSION.send(sender, "/" + mode.name + " <player> [amount]");
					return false;
				}
			} else {
				Double permissionValue = Utils.getPermissionValue(sender, "community." + mode.name);
				if(permissionValue == null) {
					points = getPoints();
				} else {
					points = permissionValue;
				}
			}
			
			Double permissionValue = Utils.getPermissionValue(sender, "community." + mode.name + ".cooldown");
			long cooldown;
			if(permissionValue == null) {
				cooldown = getCooldown();
			} else {
				cooldown = Math.round(permissionValue);
			}
			
			if(!sender.hasPermission("community." + mode.name + ".cooldown.bypass")) {
				if(sender instanceof OfflinePlayer) {
					long current = getCooldown((OfflinePlayer) sender);
					if(current > 0) {
						String formattedTime = Utils.formatTime(current);
						sendCooldownMessage(sender, formattedTime);
						return false;
					} else {
						setCooldown((OfflinePlayer) sender, cooldown);
					}
				}
			}
			
			ScoreEntry scoreEntry = plugin.getPlayerDataStorage().get(target.getUniqueId().toString());
			scoreEntry.setScore(scoreEntry.getScore() + points);
			if(sender.hasPermission("community." + mode.name + ".anonymous")) {
				sendAnonymousMessage(sender, points);
			} else {
				sendDisplayMessage(sender, sender.getName(), points);
			}
			return true;
		} else {
			sendHelpMessage(sender);
			return false;
		}
	}
	
	protected abstract double getPoints();
	
	protected abstract long getCooldown();
	
	protected abstract void sendDisplayMessage(CommandSender sender, String name, double points);
	
	protected abstract void sendAnonymousMessage(CommandSender sender, double points);
	
	protected abstract void sendCooldownMessage(CommandSender sender, String formattedTime);
	
	protected abstract void sendHelpMessage(CommandSender sender);
	
	@Override
	protected void start(BukkitRunnable runnable) {
		runnable.runTaskTimer(plugin, 0L, 20L);
	}
	
	enum Mode {
		REWARD("reward"),
		PUNISH("punish");
		
		private String name;
		
		Mode(String name) {
			this.name = name;
		}
	}
}
