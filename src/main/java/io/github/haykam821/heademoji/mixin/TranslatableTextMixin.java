package io.github.haykam821.heademoji.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.heademoji.Main;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;

@Mixin(TranslatableText.class)
public abstract class TranslatableTextMixin {
	@Shadow
	@Final
	private String key;

	@Inject(method = "visitSelf(Lnet/minecraft/text/StringVisitable$StyledVisitor;Lnet/minecraft/text/Style;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	public <T> void visitSelf(StringVisitable.StyledVisitor<T> visitor, Style style, CallbackInfoReturnable<Optional<T>> ci) {
		if (Main.MAGIC_TRANSLATION_KEY.equals(this.key)) {
			ci.setReturnValue(Main.MAGIC_TEXT.visitSelf(visitor, style));
		}
	}

	@Inject(method = "visitSelf(Lnet/minecraft/text/StringVisitable$Visitor;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	public <T> void visitSelf(StringVisitable.Visitor<T> visitor, CallbackInfoReturnable<Optional<T>> ci) {
		if (Main.MAGIC_TRANSLATION_KEY.equals(this.key)) {
			ci.setReturnValue(Main.MAGIC_TEXT.visitSelf(visitor));
		}
	}
}
