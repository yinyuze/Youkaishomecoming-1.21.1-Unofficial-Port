package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.fastprojectileapi.entity.BaseProjectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevent malformed projectile motion from producing an unbounded collision
 * search or moving an entity to invalid coordinates.
 */
@Mixin(BaseProjectile.class)
public class BaseProjectileVelocityMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	private void youkaishomecoming$sanitizeProjectileVelocity(CallbackInfo ci) {
		BaseProjectile projectile = (BaseProjectile) (Object) this;
		Vec3 velocity = projectile.getDeltaMovement();
		if (!Double.isFinite(velocity.x) || !Double.isFinite(velocity.y) || !Double.isFinite(velocity.z)
				|| velocity.lengthSqr() > 10000) {
			projectile.setDeltaMovement(Vec3.ZERO);
		}
	}

}
