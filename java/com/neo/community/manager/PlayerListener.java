package com.neo.community.manager;

import com.neo.community.Community;
import com.neo.community.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerListener implements Listener {
	private Community plugin;
	
	public PlayerListener(Community plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		Player kicked = event.getPlayer();
		String key = kicked.getUniqueId().toString();
		if(kicked.isBanned()) {
			plugin.getPlayerDataStorage().addScore(key, plugin.getSettings().getPointsOnBan());
		} else {
			plugin.getPlayerDataStorage().addScore(key, plugin.getSettings().getPointsOnKick());
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		String format = event.getFormat();
		String key = event.getPlayer().getUniqueId().toString();
		double score = plugin.getPlayerDataStorage().getScore(key);
		format = format.replace("{SCORE}", Utils.formatPoints(score));
		event.setFormat(format);
	}
}
