package com.neo.community.command;

import com.neo.community.Community;
import com.neo.community.util.Message;
import com.neo.community.util.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommunityExecutor implements CommandExecutor {
	private Community plugin;
	
	public CommunityExecutor(Community plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		OfflinePlayer player;
		
		if(args.length == 0) {
			if(!sender.hasPermission("community.self")) {
				Message.NO_PERMISSION.send(sender, "checking your community score");
				return false;
			}
			
			if(!(sender instanceof Player)) {
				Message.PLAYER_ONLY.send(sender);
				return false;
			}
			
			player = (Player) sender;
		}
		else if(args.length == 1) {
			player = Utils.getPlayerFromUsername(args[0]);
			
			if(player != sender && !sender.hasPermission("community.check")) {
				Message.NO_PERMISSION.send(sender, "checking others' community scores");
				return false;
			}
			
			if(player == null) {
				Message.PLAYER_INVALID.send(sender, args[0]);
				return false;
			}
		} else {
			Message.USAGE_COMMUNITY.send(sender);
			return false;
		}
		
		double score = plugin.getPlayerDataStorage().getScore(player.getUniqueId().toString());
		Message.SCORE_CHECK.send(sender, player.getName(), Utils.formatPoints(score));
		return true;
	}
}
