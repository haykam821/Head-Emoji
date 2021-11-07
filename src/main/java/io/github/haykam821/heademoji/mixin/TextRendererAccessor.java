package io.github.haykam821.heademoji.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;

@Mixin(TextRenderer.class)
public interface TextRendererAccessor {
	@Invoker("getFontStorage")
	public FontStorage callGetFontStorage(Identifier id);
}
