package com.neo.community.config.database;

import com.neo.community.Community;
import com.neo.community.util.Message;
import com.neo.community.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class PlayerDataStorage extends StorageAccessor<ScoreEntry> implements Listener {
	private static final int SAVE_PERIOD = 5; // minutes
	
	private Map<String, Player> onlinePlayers;
	private ActivityRunnable activityRunnable;
	
	public PlayerDataStorage(Community plugin) {
		super(plugin, "playerdata.yml");
		this.onlinePlayers = new HashMap<>();
		this.activityRunnable = new ActivityRunnable();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerId = player.getUniqueId().toString();
		onlinePlayers.put(playerId, player);
		
		boolean save = false;
		long delay = 20L;
		ScoreEntry currentEntry = get(playerId);
		if(currentEntry == null) {
			currentEntry = new ScoreEntry(player);
			currentEntry.setScore(plugin.getSettings().getFirstLoginPoints());
			
			// message for first login
			String points = Utils.formatPoints(plugin.getSettings().getFirstLoginPoints());
			Message.FIRST_LOGIN.sendDelayed(plugin, delay, player, points);
			
			save = true;
		}
		
		// update lastDayOnline
		long lastDayOnline = currentEntry.getLastDayOnline();
		long currentDay = Utils.getStartOfCurrentDay();
		if(lastDayOnline != currentDay) {
			currentEntry.setLastDayOnline(currentDay);
			currentEntry.setScore(currentEntry.getScore() + plugin.getSettings().getDailyLoginPoints());
			
			// message for logging in today
			String points = Utils.formatPoints(plugin.getSettings().getDailyLoginPoints());
			Message.DAILY_LOGIN.sendDelayed(plugin, save ? delay : (delay * 2), player, points);
			
			save = true;
		}
		
		if(save) {
			put(currentEntry);
			saveConfig();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		onlinePlayers.remove(player.getUniqueId().toString());
	}
	
	public void startTimer() {
		activityRunnable.runTaskTimer(plugin, 0L, 60 * 20L);
	}
	
	public void stopTimer() {
		activityRunnable.cancel();
	}
	
	public void saveConfig() {
		super.saveConfig();
	}
	
	@Override
	protected ScoreEntry createEntry(String key, String dataString) {
		StringTokenizer tokenizer = new StringTokenizer(dataString, " ");
		if(!tokenizer.hasMoreTokens())
			return null;
		
		ScoreEntry result = new ScoreEntry(key);
		if(tokenizer.hasMoreTokens())
			result.setScore(Double.valueOf(tokenizer.nextToken()));
		if(tokenizer.hasMoreTokens())
			result.setLastDayOnline(Long.valueOf(tokenizer.nextToken()));
		
		return result;
	}
	
	private class ActivityRunnable extends BukkitRunnable {
		private int minutesElapsed = 0;
		
		@Override
		public void run() {
			// increment scores for 1 minute passed
			for(String key : config.getKeys(false)) {
				Player online = onlinePlayers.get(key);
				ScoreEntry currentEntry = get(key);
				
				// update score
				double currentScore = currentEntry.getScore();
				double deltaScore;
				if(online != null) {
					if(plugin.getEssentialsHook() != null && plugin.getEssentialsHook().isAFK(online)) {
						deltaScore = plugin.getSettings().getAFKPPM();
					} else {
						deltaScore = plugin.getSettings().getActivePPM();
					}
				} else {
					deltaScore = plugin.getSettings().getOfflinePPM();
				}
				currentEntry.setScore(currentScore + deltaScore);
				
				// update lastDayOnline
				long lastDayOnline = currentEntry.getLastDayOnline();
				long currentDay = Utils.getStartOfCurrentDay();
				if(online != null && lastDayOnline != currentDay) {
					currentEntry.setLastDayOnline(currentDay);
					
					// message for logging in today
					Message.DAILY_LOGIN.send(online, plugin.getSettings().getDailyLoginPoints());
				}
				
				put(currentEntry);
			}
			if(minutesElapsed >= SAVE_PERIOD) {
				minutesElapsed = 0;
				saveConfig();
			}
			minutesElapsed++;
		}
	}
}
