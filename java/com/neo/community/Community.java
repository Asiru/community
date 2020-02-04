package com.neo.community;

import com.earth2me.essentials.Essentials;
import com.neo.community.command.CommunityExecutor;
import com.neo.community.command.PunishExecutor;
import com.neo.community.command.RewardExecutor;
import com.neo.community.config.Settings;
import com.neo.community.config.database.PlayerDataStorage;
import com.neo.community.hook.EssentialsHook;
import com.neo.community.hook.VotifierHook;
import com.neo.community.manager.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Community extends JavaPlugin {
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
		registerListener(new PlayerListener(this));
		
		registerCommand("community", new CommunityExecutor(this));
		registerCommand("reward", new RewardExecutor(this));
		registerCommand("punish", new PunishExecutor(this));
		
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
	
	private void registerCommand(String name, CommandExecutor executor) {
		PluginCommand command = getCommand(name);
		if(command != null) {
			command.setExecutor(executor);
			if(executor instanceof TabCompleter)
				command.setTabCompleter((TabCompleter) executor);
		} else
			getLogger().log(Level.SEVERE, "Could not register command: /" + name);
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
