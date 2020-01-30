package com.neo.community.hook;

import com.neo.community.Community;
import com.neo.community.config.database.PlayerDataStorage;
import com.neo.community.config.database.ScoreEntry;
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
			PlayerDataStorage pds = plugin.getPlayerDataStorage();
			ScoreEntry currentEntry = pds.get(player.getUniqueId().toString());
			double currentScore = currentEntry.getScore();
			currentEntry.setScore(currentScore + plugin.getSettings().getPointsOnVote());
		}
	}
}
