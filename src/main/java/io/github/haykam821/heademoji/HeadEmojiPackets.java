package io.github.haykam821.heademoji;

import java.util.Optional;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class HeadEmojiPackets {
	private static final Identifier REGISTER_EMOJI_ID = new Identifier(Main.MOD_ID, "register_emoji");
	private static final Identifier REQUEST_EMOJI_ID = new Identifier(Main.MOD_ID, "request_emoji");

	private HeadEmojiPackets() {
		return;
	}

	protected static void register() {
		ClientPlayConnectionEvents.JOIN.register(HeadEmojiPackets::onJoin);
		ClientPlayNetworking.registerGlobalReceiver(HeadEmojiPackets.REGISTER_EMOJI_ID, HeadEmojiPackets::onRegisterEmoji);
	}

	private static void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
		Main.SERVER_REGISTRY.clear();
		sender.sendPacket(HeadEmojiPackets.createRequestEmojiPacket());
	}

	public static Packet<?> createRequestEmojiPacket() {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		Optional<ModContainer> optional = FabricLoader.getInstance().getModContainer(Main.MOD_ID);
		if (optional.isPresent()) {
			buf.writeString(Main.MOD_ID);
			buf.writeString(optional.get().getMetadata().getVersion().getFriendlyString());
		} else {
			buf.writeString("");
			buf.writeString("");
		}

		return ClientPlayNetworking.createC2SPacket(HeadEmojiPackets.REQUEST_EMOJI_ID, buf);
	}

	private static void onRegisterEmoji(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		if (!"head".equals(buf.readString())) return;

		String key = buf.readString();
		String texture = buf.readString();

		if (texture.isEmpty()) {
			Main.SERVER_REGISTRY.unregister(key);
		} else {
			Main.SERVER_REGISTRY.register(key, texture);
		}
	}
}
