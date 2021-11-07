package io.github.haykam821.heademoji.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.haykam821.heademoji.HeadEmojiCollector;
import io.github.haykam821.heademoji.HeadEmojiRenderer;
import io.github.haykam821.heademoji.Main;
import io.github.haykam821.heademoji.registry.HeadEmojiAssigned;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

@Mixin(ChatHud.class)
public class ChatHudMixin {
	@Unique
	private HeadEmojiRenderer headEmojiRenderer;

	@Unique
	private final HeadEmojiCollector headEmojiCollector = new HeadEmojiCollector();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void initHeadEmojiRenderer(MinecraftClient client, CallbackInfo ci) {
		this.headEmojiRenderer = new HeadEmojiRenderer(client);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderEmojiForLine(MatrixStack matrices, int tickDelta, CallbackInfo ci, int visibleLineCount, int visibleMessagesSize, boolean chatFocused, float chatScale, int scaledWidth, double chatOpacity, double textBackgroundOpacity, double j, double k, int l, int m, ChatHudLine<OrderedText> line, double alpha, int p, int q, int r, double s) {
		this.headEmojiRenderer.render(matrices, line, Math.floor(s + k), (float) alpha);
	}

	@Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"))
	private void collectHeadEmojis(Text message, int messageId, int timestamp, boolean refresh, CallbackInfo ci) {
		this.headEmojiCollector.clear();
		this.headEmojiCollector.collectAll(message);
	}

	@Redirect(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At(value = "NEW", target = "net/minecraft/client/gui/hud/ChatHudLine", ordinal = 0))
	private ChatHudLine<OrderedText> assignHeadEmojis(int timestamp, Object object, int messageId) {
		OrderedText text = (OrderedText) object;
		ChatHudLine<OrderedText> line = new ChatHudLine<>(timestamp, text, messageId);

		if (!headEmojiCollector.isEmpty()) {
			List<String> keys = new ArrayList<>();
			text.accept((index, style, codePoint) -> {
				if (codePoint == Main.MAGIC_CHARACTER) {
					keys.add(headEmojiCollector.getKey());
				}
				return true;
			});

			((HeadEmojiAssigned) line).assignKeys(keys);
		}

		return line;
	}
}
