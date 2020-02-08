package com.neo.community.hook;

import com.neo.community.Community;
import com.neo.community.config.database.ScoreEntry;
import com.neo.community.util.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderApiHook extends PlaceholderExpansion {
	private static final String IDENTIFIER = "community";
	
	private Community plugin;
	
	public PlaceholderApiHook(Community plugin) {
		this.plugin = plugin;
	}
	
	private String handleRequest(OfflinePlayer p, String identifier) {
		if(identifier.equals("score")) {
			String key = p.getUniqueId().toString();
			return Utils.formatPoints(plugin.getPlayerDataStorage().getScore(key));
		} else if(identifier.startsWith("topscore")) {
			identifier = identifier.replace("topscore", "");
			try {
				int index = Integer.valueOf(identifier);
				if(index < 1) {
					throw new NumberFormatException();
				}
				
				ScoreEntry entry = plugin.getPlayerDataStorage().getEntry(index - 1);
				if(entry == null) {
					return "empty";
				}
				return Utils.formatPoints(entry.getScore());
			} catch(NumberFormatException ex) {
				return "invalid index";
			}
		} else if(identifier.startsWith("topplayer")) {
			identifier = identifier.replace("topplayer", "");
			try {
				int index = Integer.valueOf(identifier);
				if(index < 1) {
					throw new NumberFormatException();
				}
				
				ScoreEntry entry = plugin.getPlayerDataStorage().getEntry(index - 1);
				if(entry == null) {
					return "empty";
				}
				OfflinePlayer target = Utils.getPlayerFromUsername(entry.getKey());
				if(target == null) {
					return "invalid player";
				} else {
					return target.getName();
				}
			} catch(NumberFormatException ex) {
				return "invalid index";
			}
		}
		return null;
	}
	
	@Override
	public String onRequest(OfflinePlayer p, String identifier) {
		return handleRequest(p, identifier);
	}
	
	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}
	
	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}
	
	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}
}
