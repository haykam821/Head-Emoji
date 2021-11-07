package io.github.haykam821.heademoji.registry;

import java.util.HashMap;
import java.util.Map;

import com.mojang.authlib.GameProfile;

/**
 * A simple head emoji registry implementation.
 */
public class SimpleHeadEmojiRegistry implements HeadEmojiRegistry {
	private final Map<String, GameProfile> registry = new HashMap<>();

	@Override
	public GameProfile get(String key) {
		return this.registry.get(key);
	}

	@Override
	public int size() {
		return this.registry.size();
	}

	@Override
	public boolean register(String key, GameProfile profile) {
		this.registry.put(key, profile);
		return true;
	}

	@Override
	public boolean unregister(String key) {
		this.registry.remove(key);
		return true;
	}

	@Override
	public boolean clear() {
		this.registry.clear();
		return true;
	}

	@Override
	public String toString() {
		return "SimpleHeadEmojiRegistry{registry=" + this.registry + "}";
	}
}
