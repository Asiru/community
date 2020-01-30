package com.neo.community;

import com.earth2me.essentials.Essentials;
import com.neo.community.config.Settings;
import com.neo.community.config.database.PlayerDataStorage;
import com.neo.community.hook.EssentialsHook;
import com.neo.community.hook.VotifierHook;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Community extends JavaPlugin implements Listener {
	private Settings settings;
	private EssentialsHook essentialsHook;
	private PlayerDataStorage playerDataStorage;
	
	@Override
	public void onEnable() {
		settings = new Settings(this);
		essentialsHook = connectEssentials();
		playerDataStorage = new PlayerDataStorage(this);
		
		registerListener(connectVotifier());
		registerListener(playerDataStorage);
		
		playerDataStorage.startTimer();
	}
	
	@Override
	public void onDisable() {
		 playerDataStorage.saveConfig();
		 playerDataStorage.stopTimer();
	}
	
	private void registerListener(Listener listener) {
		if(listener != null) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}
	
	private EssentialsHook connectEssentials() {
		if(Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
			Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
			if(essentials != null) {
				return new EssentialsHook(essentials);
			}
		}
		return null;
	}
	
	private VotifierHook connectVotifier() {
		if(Bukkit.getPluginManager().isPluginEnabled("NuVotifier")) {
			return new VotifierHook(this);
		}
		return null;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public EssentialsHook getEssentialsHook() {
		return essentialsHook;
	}
	
	public PlayerDataStorage getPlayerDataStorage() {
		return playerDataStorage;
	}
}
