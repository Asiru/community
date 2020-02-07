package com.neo.community.manager;

import com.neo.community.Community;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerListener implements Listener {
	private Community plugin;
	
	public PlayerListener(Community plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player kicked = event.getPlayer();
		String key = kicked.getUniqueId().toString();
		if(kicked.isBanned()) {
			plugin.getPlayerDataStorage().addScore(key, plugin.getSettings().getPointsOnBan());
		} else {
			plugin.getPlayerDataStorage().addScore(key, plugin.getSettings().getPointsOnKick());
		}
	}
}
