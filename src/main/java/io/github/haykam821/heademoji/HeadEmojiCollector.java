package io.github.haykam821.heademoji;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class HeadEmojiCollector {
	private final List<String> emojiKeys = new ArrayList<>();

	public void collectAll(Text text) {
		this.collect(text);
		for (Text sibling : text.getSiblings()) {
			this.collectAll(sibling);
		}
	}

	private void collect(Text text) {
		if (text instanceof TranslatableText) {
			TranslatableText translatableText = (TranslatableText) text;
			if (Main.MAGIC_TRANSLATION_KEY.equals(translatableText.getKey())) {
				int argCount = translatableText.getArgs().length;
				if (argCount == 1) {
					// Args in format [key]
					this.addKey(translatableText, 0);
				} else if (argCount == 2) {
					// Args in format [fallback, key]
					this.addKey(translatableText, 1);
				}
			}
		}
	}

	private void addKey(TranslatableText text, int index) {
		Object keyObject = text.getArgs()[index];
		String key = keyObject instanceof Text ? ((Text) keyObject).asString() : keyObject.toString();
		System.out.println(key);
		this.emojiKeys.add(key);
	}

	public String getKey() {
		// Tried to assign more keys than collected
		if (this.emojiKeys.isEmpty()) {
			return null;
		}

		return this.emojiKeys.remove(0);
	}

	public boolean isEmpty() {
		return this.emojiKeys.isEmpty();
	}

	public void clear() {
		this.emojiKeys.clear();
	}

	@Override
	public String toString() {
		return "HeadEmojiCollector{emojiKeys=" + this.emojiKeys + "}";
	}
}
