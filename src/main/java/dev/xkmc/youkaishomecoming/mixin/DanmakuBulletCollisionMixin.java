package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.danmakuapi.content.entity.DanmakuBulletEntity;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Danmaku-to-danmaku collision has no gameplay effect and scales quadratically
 * in dense patterns, so reject it even if another mod changes the entity cache.
 */
@Mixin(DanmakuBulletEntity.class)
public class DanmakuBulletCollisionMixin {

	@Inject(method = "canHitEntity", at = @At("HEAD"), cancellable = true)
	private void youkaishomecoming$skipProjectileCollision(Entity target,
			CallbackInfoReturnable<Boolean> cir) {
		if (target instanceof SimplifiedProjectile) {
			cir.setReturnValue(false);
		}
	}

}
