package com.neo.community.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public enum Message {
	NO_PERMISSION("&cYou do not have permssion for %s."),
	PLAYER_INVALID("&cCannot find player called \'%s\'."),
	PLAYER_SELF("&cYou cannot target yourself."),
	PUNISH_COOLDOWN("&cYou cannot give another community punishment for %s."),
	PUNISH_MESSAGE("&c%s &6has removed &c%d &6of your community points!"),
	PUNISH_ANONYMOUS("&6You have lost &c%d &6community points!"),
	REWARD_COOLDOWN("&cYou cannot give another community reward for %s."),
	REWARD_MESSAGE("&e%s &6has given you &e%d &6community points!"),
	REWARD_ANONYMOUS("&6You have been given &e%d &6community points!"),
	AMOUNT_INVALID("&c\'%s\' is not a valid point value."),
	FIRST_LOGIN("&6You\'ve received &e%s &6community points for joining the server!"),
	DAILY_LOGIN("&6You\'ve received &e%s &6community points for logging in today!");
	
	private String message;
	
	Message(String message) {
		this.message = message;
	}
	
	public void send(CommandSender sender, Object... args) {
		if(sender == null) {
			return;
		}
		
		String msg = this.message;
		for(Object arg : args) {
			msg = msg.replaceFirst("%[ds]", String.valueOf(arg));
		}
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		
		sender.sendMessage(msg);
	}
	
	public void sendDelayed(Plugin plugin, long delay, CommandSender sender, Object... args) {
		new MessageRunnable(sender, args).runTaskLater(plugin, delay);
	}
	
	private class MessageRunnable extends BukkitRunnable {
		private CommandSender sender;
		private Object[] args;
		
		private MessageRunnable(CommandSender sender, Object... args) {
			this.sender = sender;
			this.args = args;
		}
		
		@Override
		public void run() {
			send(sender, args);
		}
	}
}
