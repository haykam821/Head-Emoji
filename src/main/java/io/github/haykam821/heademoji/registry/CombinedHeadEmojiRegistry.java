package io.github.haykam821.heademoji.registry;

import com.mojang.authlib.GameProfile;

/**
 * An immutable head emoji registry that combines multiple registries.
 */
public class CombinedHeadEmojiRegistry extends ImmutableHeadEmojiRegistry {
	private final HeadEmojiRegistry[] registries;

	public CombinedHeadEmojiRegistry(HeadEmojiRegistry... registries) {
		this.registries = registries;
	}

	@Override
	public GameProfile get(String key) {
		for (HeadEmojiRegistry registry : this.registries) {
			GameProfile profile = registry.get(key);
			if (profile != null) return profile;
		}
		return null;
	}

	@Override
	public int size() {
		int size = 0;
		for (HeadEmojiRegistry registry : this.registries) {
			size += registry.size();
		}
		return size;
	}

	@Override
	public String toString() {
		return "CombinedHeadEmojiRegistry{registries=" + this.registries + "}";
	}
}
