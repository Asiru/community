package com.neo.community.command;

import com.neo.community.Community;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PunishExecutor implements CommandExecutor {
	private Community plugin;
	
	public PunishExecutor(Community plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
}
