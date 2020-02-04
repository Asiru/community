package com.neo.community.command;

import com.neo.community.Community;
import com.neo.community.util.Message;
import org.bukkit.command.CommandSender;

public class RewardExecutor extends PointExecutor {
	public RewardExecutor(Community plugin) {
		super(plugin, Mode.REWARD);
	}
	
	@Override
	protected double getPoints() {
		return plugin.getSettings().getRewardPoints();
	}
	
	@Override
	protected long getCooldown() {
		return plugin.getSettings().getRewardCooldown();
	}
	
	@Override
	protected void sendDisplayMessage(CommandSender sender, String name, double points) {
		Message.REWARD_MESSAGE.send(sender, name, points);
	}
	
	@Override
	protected void sendAnonymousMessage(CommandSender sender, double points) {
		Message.REWARD_ANONYMOUS.send(sender, points);
	}
	
	@Override
	protected void sendCooldownMessage(CommandSender sender, String formattedTime) {
		Message.REWARD_COOLDOWN.send(sender, formattedTime);
	}
}
