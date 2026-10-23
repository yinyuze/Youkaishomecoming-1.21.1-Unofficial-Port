package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.danmakuapi.api.GrazeHelper;
import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import dev.xkmc.youkaishomecoming.content.attachment.graze.GrazeHelperGL;
import dev.xkmc.youkaishomecoming.init.registrate.GLMeta;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrazeHelper.class)
public class GrazeHelperMixin {

	@Inject(method = "graze", at = @At("HEAD"), cancellable = true)
	private static void youkaishomecoming$graze(Player player, GrazingEntity entity, CallbackInfo ci) {
		GrazeHelperGL.graze(player, entity);
		ci.cancel();
	}

	@Inject(method = "getHitBoxShrink", at = @At("HEAD"), cancellable = true)
	private static void youkaishomecoming$getHitBoxShrink(Player player, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(GrazeHelperGL.getHitBoxDelta(player));
	}

	@Inject(method = "shouldPlayerHurt", at = @At("HEAD"), cancellable = true)
	private static void youkaishomecoming$shouldPlayerHurt(Player player, LivingEntity target,
			CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(GLMeta.GRAZE.type().getOrCreate(player).shouldHurt(player, target));
	}

	@Inject(method = "forbidDanmaku", at = @At("HEAD"), cancellable = true)
	private static void youkaishomecoming$forbidDanmaku(Player player, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(GrazeHelperGL.forbidDanmaku(player));
	}

}
