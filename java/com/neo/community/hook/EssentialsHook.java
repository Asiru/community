package com.neo.community.hook;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class EssentialsHook {
	private Essentials essentials;
	
	public EssentialsHook(@Nonnull Essentials essentials) {
		this.essentials = essentials;
	}
	
	public boolean isAFK(Player p) {
		if(essentials != null && p != null) {
			User user = essentials.getUser(p);
			if(user != null)
				return user.isAfk();
		}
		return false;
	}
}
