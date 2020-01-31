package com.neo.community.command;

import com.neo.community.Community;
import com.neo.community.config.database.ScoreEntry;
import com.neo.community.manager.CooldownManager;
import com.neo.community.util.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RewardExecutor extends CooldownManager<OfflinePlayer> implements CommandExecutor {
	private Community plugin;
	
	public RewardExecutor(Community plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			// TODO message for players only
			return false;
		}
		Player player = (Player) sender;
		
		// check permission "community.reward"
		if(args.length >= 1) {
			OfflinePlayer target = Utils.getPlayerFromUsername(args[0]);
			if(target == null) {
				// TODO error message for invalid target player
				return false;
			}
			
			double points;
			if(args.length >= 2) {
				// check permission "community.reward.bypass"
				try {
					points = Double.valueOf(args[1]);
				} catch(NumberFormatException ex) {
					// TODO error message for invalid point value
					return false;
				}
			} else {
				points = plugin.getSettings().getRewardPoints();
			}
			
			// check permission "community.reward.cooldown.bypass"
			if(getCooldown(player) > 0) {
				// TODO error message for cooldown active
				return false;
			}
			
			ScoreEntry scoreEntry = plugin.getPlayerDataStorage().get(target.getUniqueId().toString());
			scoreEntry.setScore(scoreEntry.getScore() + points);
			// TODO set cooldown
			
			// check permission "community.reward.anonymous"
			// TODO success messages for sender and target
			return true;
		}
		return false;
	}
	
	@Override
	protected void start(BukkitRunnable runnable) {
		runnable.runTaskTimer(plugin, 0L, 20L);
	}
}
