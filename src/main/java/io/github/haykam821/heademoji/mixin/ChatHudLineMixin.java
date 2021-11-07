package io.github.haykam821.heademoji.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import io.github.haykam821.heademoji.registry.HeadEmojiAssigned;
import net.minecraft.client.gui.hud.ChatHudLine;

@Mixin(ChatHudLine.class)
public class ChatHudLineMixin implements HeadEmojiAssigned {
	private List<String> headEmoji;

	@Override
	public void assignKeys(List<String> headEmoji) {
		this.headEmoji = headEmoji;
	}

	@Override
	public String getKey(int index) {
		if (this.headEmoji == null) return null;

		if (index < 0) return null;
		if (index >= this.headEmoji.size()) return null;

		return this.headEmoji.get(index);
	}

	@Override
	public int getSize() {
		if (this.headEmoji == null) return 0;
		return this.headEmoji.size();
	}
}
