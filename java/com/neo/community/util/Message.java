package com.neo.community.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public enum Message {
	FIRST_LOGIN("&6You\'ve received &e%s &6points for joining the server!"),
	DAILY_LOGIN("&6You\'ve received &e%s &6points for logging in today!");
	
	private String message;
	
	Message(String message) {
		this.message = message;
	}
	
	public void send(Player p, Object... args) {
		if(p == null) {
			return;
		}
		
		String msg = this.message;
		for(Object arg : args) {
			msg = msg.replaceFirst("%[ds]", String.valueOf(arg));
		}
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		
		p.sendMessage(msg);
	}
	
	public void sendDelayed(Plugin plugin, long delay, Player p, Object... args) {
		new MessageRunnable(p, args).runTaskLater(plugin, delay);
	}
	
	private class MessageRunnable extends BukkitRunnable {
		private Player p;
		private Object[] args;
		
		private MessageRunnable(Player p, Object... args) {
			this.p = p;
			this.args = args;
		}
		
		@Override
		public void run() {
			send(p, args);
		}
	}
}
