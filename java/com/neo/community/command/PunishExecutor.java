package com.neo.community.command;

import com.neo.community.Community;
import com.neo.community.util.Message;
import org.bukkit.command.CommandSender;

public class PunishExecutor extends PointExecutor {
	public PunishExecutor(Community plugin) {
		super(plugin, Mode.PUNISH);
	}
	
	@Override
	protected double getPoints() {
		return plugin.getSettings().getPunishPoints();
	}
	
	@Override
	protected long getCooldown() {
		return plugin.getSettings().getPunishCooldown();
	}
	
	@Override
	protected void sendDisplayMessage(CommandSender sender, String name, double points) {
		Message.PUNISH_MESSAGE.send(sender, name, points);
	}
	
	@Override
	protected void sendAnonymousMessage(CommandSender sender, double points) {
		Message.PUNISH_ANONYMOUS.send(sender, points);
	}
	
	@Override
	protected void sendCooldownMessage(CommandSender sender, String formattedTime) {
		Message.PUNISH_COOLDOWN.send(sender, formattedTime);
	}
	
	@Override
	protected void sendHelpMessage(CommandSender sender) {
		Message.USAGE_PUNISH.send(sender);
	}
}
