package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.danmakuapi.content.entity.DanmakuBulletEntity;
import dev.xkmc.danmakuapi.content.entity.DanmakuLaserEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * EntityCulling treats {@link Entity#noCulling} as an explicit opt-out on 1.21.1.
 * Danmaku are rendered from DanmakuAPI's virtual client cache, so occlusion state
 * maintained for normal level entities must not suppress them.
 */
@Mixin({DanmakuBulletEntity.class, DanmakuLaserEntity.class})
public class DanmakuEntityCullingMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void youkaishomecoming$disableExternalEntityCulling(CallbackInfo ci) {
		((Entity) (Object) this).noCulling = true;
	}

}
