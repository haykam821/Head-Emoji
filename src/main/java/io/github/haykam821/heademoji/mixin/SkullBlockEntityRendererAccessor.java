package io.github.haykam821.heademoji.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;

@Mixin(SkullBlockEntityRenderer.class)
public interface SkullBlockEntityRendererAccessor {
	@Accessor("MODELS")
	public Map<SkullBlock.SkullType, SkullBlockEntityModel> getModels();
}
