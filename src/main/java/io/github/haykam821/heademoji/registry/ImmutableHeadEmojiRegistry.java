package io.github.haykam821.heademoji.registry;

import com.mojang.authlib.GameProfile;

/**
 * A head emoji registry that cannot be modified.
 */
public abstract class ImmutableHeadEmojiRegistry implements HeadEmojiRegistry {
	@Override
	public boolean register(String key, GameProfile profile) {
		return false;
	}

	@Override
	public boolean register(String key, String texture) {
		return false;
	}

	@Override
	public boolean unregister(String key) {
		return false;
	}

	@Override
	public boolean clear() {
		return false;
	}
}
