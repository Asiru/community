package com.neo.community.manager;

import com.neo.community.Community;
import com.neo.community.config.database.ScoreEntry;
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
		ScoreEntry entry = plugin.getPlayerDataStorage().get(key);
		double score = entry.getScore();
		if(kicked.isBanned()) {
			entry.setScore(score + plugin.getSettings().getPointsOnBan());
		} else {
			entry.setScore(score + plugin.getSettings().getPointsOnKick());
		}
	}
}
