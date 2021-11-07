package io.github.haykam821.heademoji;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import io.github.haykam821.heademoji.mixin.TextRendererAccessor;
import io.github.haykam821.heademoji.registry.HeadEmojiAssigned;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public final class HeadEmojiRenderer {
	private final MinecraftClient client;

	public HeadEmojiRenderer(MinecraftClient client) {
		this.client = client;
	}

	public void render(MatrixStack matrices, ChatHudLine<OrderedText> line, double y, float alpha) {
		int fontHeight = this.client.textRenderer.fontHeight;
		int scale = fontHeight * 2;

		matrices.push();
		matrices.translate(0, y + fontHeight - 1, 0);

		VertexConsumerProvider.Immediate vertexConsumers = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

		SkullBlockEntityModel model = new SkullEntityModel(this.client.getEntityModelLoader().getModelPart(EntityModelLayers.PLAYER_HEAD));
		model.setHeadRotation(0, 180, 0);

		Iterator<GameProfile> profileIterator = ((HeadEmojiAssigned) line).iterator();

		line.getText().accept((index, style, codePoint) -> {
			FontStorage fontStorage = ((TextRendererAccessor) this.client.textRenderer).callGetFontStorage(style.getFont());
			Glyph glyph = fontStorage.getGlyph(codePoint);

			if (codePoint == Main.MAGIC_CHARACTER) {
				matrices.push();

				matrices.translate(scale / 4d, 0, 0);
				matrices.scale(scale, scale, -1);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

				GameProfile profile = profileIterator.next();
				VertexConsumer vertexConsumer = this.getVertexConsumer(vertexConsumers, profile);

				model.render(matrices, vertexConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1, 1, 1, alpha);

				matrices.pop();
			}

			float advance = glyph.getAdvance(style.isBold());
			matrices.translate(advance, 0, 0);

			return true;
		});

		vertexConsumers.draw();
		matrices.pop();
	}

	private VertexConsumer getVertexConsumer(VertexConsumerProvider vertexConsumers, GameProfile profile) {
		Identifier skinId = this.getSkinIdOrDefault(profile);
		RenderLayer layer = RenderLayer.getEntityTranslucent(skinId);
		return vertexConsumers.getBuffer(layer);
	}

	private Identifier getSkinIdOrDefault(GameProfile profile) {
		Identifier skinId = this.getSkinId(profile);
		if (skinId != null) return skinId;

		if (profile == null) return DefaultSkinHelper.getTexture();

		UUID uuid = PlayerEntity.getUuidFromProfile(profile);
		return DefaultSkinHelper.getTexture(uuid);
	}

	private Identifier getSkinId(GameProfile profile) {
		if (profile == null) return null;
		PlayerSkinProvider skinProvider = this.client.getSkinProvider();

		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> skinTextures = skinProvider.getTextures(profile);
		if (skinTextures == null) return null;
		if (!skinTextures.containsKey(MinecraftProfileTexture.Type.SKIN)) return null;

		return skinProvider.loadSkin(skinTextures.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
	}

	@Override
	public String toString() {
		return "HeadEmojiRenderer{client=" + this.client + "}";
	}
}
