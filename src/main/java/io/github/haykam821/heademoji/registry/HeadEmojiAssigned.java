package io.github.haykam821.heademoji.registry;

import java.util.Iterator;
import java.util.List;

import com.mojang.authlib.GameProfile;

import io.github.haykam821.heademoji.Main;

public interface HeadEmojiAssigned extends Iterable<GameProfile> {
	public void assignKeys(List<String> headEmoji);

	public String getKey(int index);

	public int getSize();

	public default GameProfile getHeadEmoji(int index) {
		return Main.REGISTRY.get(this.getKey(index));
	}

	@Override
	public default Iterator<GameProfile> iterator() {
		return new Iterator<GameProfile>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return this.index < HeadEmojiAssigned.this.getSize();
			}

			@Override
			public GameProfile next() {
				GameProfile profile = HeadEmojiAssigned.this.getHeadEmoji(this.index);
				this.index += 1;
				return profile;
			}
		};
	}
}
