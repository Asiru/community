package com.neo.community.config.database;

import com.neo.community.Community;
import com.neo.community.config.ConfigAccessor;

import java.util.HashMap;
import java.util.Map;

public abstract class StorageAccessor<E extends StorageEntry> extends ConfigAccessor<Community> {
	private Map<String, E> entries;
	
	StorageAccessor(Community plugin, String fileName) {
		super(plugin, true, fileName, "data");
		this.entries = new HashMap<>();
		load();
	}
	
	public void put(E entry) {
		entries.put(entry.getKey(), entry);
		config.set(entry.getKey(), entry.getData());
	}
	
	public void remove(String key) {
		entries.remove(key);
		config.set(key, null);
	}
	
	public E get(String key) {
		return entries.get(key);
	}
	
	private void load() {
		for(String key : config.getKeys(false)) {
			E entry = createEntry(key, config.getString(key));
			if(entry != null) {
				entries.put(key, entry);
			}
		}
	}
	
	protected abstract E createEntry(String key, String dataString);
}
