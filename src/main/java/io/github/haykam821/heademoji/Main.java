package io.github.haykam821.heademoji;

import io.github.haykam821.heademoji.command.HeadEmojiCommand;
import io.github.haykam821.heademoji.registry.CombinedHeadEmojiRegistry;
import io.github.haykam821.heademoji.registry.HeadEmojiRegistry;
import io.github.haykam821.heademoji.registry.SimpleHeadEmojiRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Main implements ClientModInitializer {
	protected static final String MOD_ID = "heademoji";

	private static final int MAGIC_NUMBER = 0xEF04;
	public static final char MAGIC_CHARACTER = (char) Main.MAGIC_NUMBER;
	public static final String MAGIC_TRANSLATION_KEY = "%1$s%" + Main.MAGIC_NUMBER + "$s";
	public static final Text MAGIC_TEXT = new LiteralText("" + Main.MAGIC_CHARACTER);

	public static final HeadEmojiRegistry CLIENT_REGISTRY = new SimpleHeadEmojiRegistry();
	public static final HeadEmojiRegistry SERVER_REGISTRY = new SimpleHeadEmojiRegistry();
	public static final HeadEmojiRegistry REGISTRY = new CombinedHeadEmojiRegistry(CLIENT_REGISTRY, SERVER_REGISTRY);

	@Override
	public void onInitializeClient() {
		HeadEmojiCommand.register(ClientCommandManager.DISPATCHER);
		HeadEmojiPackets.register();
	}
}
