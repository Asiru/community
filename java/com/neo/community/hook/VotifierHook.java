package com.neo.community.hook;

import com.neo.community.Community;
import com.neo.community.util.Utils;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierHook implements Listener {
	private Community plugin;
	
	public VotifierHook(Community plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onVote(VotifierEvent event) {
		// award points
		Vote vote = event.getVote();
		OfflinePlayer player = Utils.getPlayerFromUsername(vote.getUsername());
		if(player != null) {
			String key = player.getUniqueId().toString();
			plugin.getPlayerDataStorage().addScore(key, plugin.getSettings().getPointsOnVote());
		}
	}
}
