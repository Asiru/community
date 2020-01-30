package com.neo.community.config;

import com.neo.community.Community;

public final class Settings extends ConfigAccessor<Community> {
	private static final String
			ACTIVE_PPM = "points.per-minute.active",
			AFK_PPM = "points.per-minute.afk",
			OFFLINE_PPM = "points.per-minute.offline",
			FIRST_LOGIN_POINTS = "points.login.first",
			DAILY_LOGIN_POINTS = "points.login.daily",
			POINTS_COMMEND = "points.given.commend",
			POINTS_PUNISH = "points.given.punish",
			POINTS_ON_VOTE = "points.on-vote",
			POINTS_ON_KICK = "points.on-kick",
			POINTS_ON_BAN = "points.on-ban";
	
	public Settings(Community plugin) {
		super(plugin, true, "config.yml");
	}
	
	public double getActivePPM() {
		return config.getDouble(ACTIVE_PPM, 3.6);
	}
	
	public double getAFKPPM() {
		return config.getDouble(AFK_PPM, -0.12);
	}
	
	public double getOfflinePPM() {
		return config.getDouble(OFFLINE_PPM, -0.12);
	}
	
	public double getFirstLoginPoints() {
		return config.getDouble(FIRST_LOGIN_POINTS, 1000);
	}
	
	public double getDailyLoginPoints() {
		return config.getDouble(DAILY_LOGIN_POINTS, 15);
	}
	
	public double getCommendPoints() {
		return config.getDouble(POINTS_COMMEND, 100);
	}
	
	public double getPunishPoints() {
		return config.getDouble(POINTS_PUNISH, -100);
	}
	
	public double getPointsOnVote() {
		return config.getDouble(POINTS_ON_VOTE, 25);
	}
	
	public double getPointsOnKick() {
		return config.getDouble(POINTS_ON_KICK, -1000);
	}
	
	public double getPointsOnBan() {
		return config.getDouble(POINTS_ON_BAN, -10000);
	}
}
