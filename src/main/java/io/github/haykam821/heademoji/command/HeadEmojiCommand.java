package io.github.haykam821.heademoji.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import io.github.haykam821.heademoji.HeadEmojiPackets;
import io.github.haykam821.heademoji.Main;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.TranslatableText;

public final class HeadEmojiCommand {
	private HeadEmojiCommand() {
		return;
	}

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("heademoji")
			.then(ClientCommandManager.literal("register")
				.then(ClientCommandManager.argument("key", StringArgumentType.string())
					.then(ClientCommandManager.argument("texture", StringArgumentType.greedyString())
						.executes(HeadEmojiCommand::executeRegister))))
			.then(ClientCommandManager.literal("clear")
				.executes(HeadEmojiCommand::executeClear)));
	}

	private static int executeRegister(CommandContext<FabricClientCommandSource> context) {
		String key = StringArgumentType.getString(context, "key");
		String texture = StringArgumentType.getString(context, "texture");

		if (Main.CLIENT_REGISTRY.register(key, texture)) {
			context.getSource().sendFeedback(new TranslatableText("command.heademoji.register.success", key));
			return Command.SINGLE_SUCCESS;
		} else {
			context.getSource().sendError(new TranslatableText("command.heademoji.register.failure", key));
			return 0;
		}
	}

	private static int executeClear(CommandContext<FabricClientCommandSource> context) {
		FabricClientCommandSource source = context.getSource();

		int clientSize = Main.CLIENT_REGISTRY.size();
		Main.CLIENT_REGISTRY.clear();

		int serverSize = Main.SERVER_REGISTRY.size();
		Main.SERVER_REGISTRY.clear();

		ClientPlayNetworkHandler networkHandler = source.getClient().getNetworkHandler();
		networkHandler.sendPacket(HeadEmojiPackets.createRequestEmojiPacket());

		source.sendFeedback(new TranslatableText("command.heademoji.clear.success", clientSize, serverSize));
		return clientSize + serverSize;
	}
}
