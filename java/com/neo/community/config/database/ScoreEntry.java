package com.neo.community.config.database;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class ScoreEntry implements StorageEntry {
	private static final DecimalFormat DF_SCORE = new DecimalFormat("0.00");
	
	private final String playerId;
	
	private double score;
	private long lastDayOnline;
	
	ScoreEntry(Player player) {
		this(player.getUniqueId().toString(), 0, 0);
	}
	
	ScoreEntry(String playerId) {
		this(playerId, 0, 0);
	}
	
	private ScoreEntry(String playerId, double score, long lastDayOnline) {
		this.playerId = playerId;
		this.score = score;
		this.lastDayOnline = lastDayOnline;
	}
	
	@Override
	public String getKey() {
		return playerId;
	}
	
	@Override
	public String getData() {
		return String.join(" ", DF_SCORE.format(score), Long.toString(lastDayOnline));
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	long getLastDayOnline() {
		return lastDayOnline;
	}
	
	void setLastDayOnline(long lastDayOnline) {
		this.lastDayOnline = lastDayOnline;
	}
}
