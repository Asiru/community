package com.neo.community.manager;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CooldownManager<T> {
	private final Map<T, Cooldown> cooldowns = new ConcurrentHashMap<>();
	
	protected final long getCooldown(T object) {
		if(cooldowns.containsKey(object))
			return cooldowns.get(object).cooldown;
		return 0;
	}
	
	protected final void setCooldown(T object, long cooldown) {
		Cooldown c = cooldowns.get(object);
		if(c == null) {
			c = new Cooldown(object, cooldown);
			cooldowns.put(object, c);
			start(c);
		} else {
			c.cooldown = cooldown;
		}
	}
	
	protected abstract void start(BukkitRunnable runnable);
	
	private class Cooldown extends BukkitRunnable {
		private T object;
		private long cooldown;
		
		private Cooldown(T object, long cooldown) {
			this.object = object;
			this.cooldown = cooldown;
		}
		
		@Override
		public void run() {
			cooldown--;
			if(cooldown <= 0) {
				cancel();
				cooldowns.remove(object);
			}
		}
	}
}
