package dev.xkmc.youkaishomecoming.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.xkmc.danmakuapi.content.entity.DanmakuBulletEntity;
import dev.xkmc.danmakuapi.content.entity.DanmakuLaserEntity;
import dev.xkmc.danmakuapi.content.spell.item.PlayerHolder;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.MaidDanmakuTask;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHolder.class)
public class PlayerHolderMixin {

	@Inject(method = "shoot", at = @At("HEAD"))
	private void youkaishomecoming$buffMaidDanmaku(SimplifiedProjectile projectile, CallbackInfo ci) {
		LivingEntity owner = ((PlayerHolder) (Object) this).player();
		if (owner instanceof EntityMaid maid) {
			float multiplier = MaidDanmakuTask.getFavorabilityMultiplier(maid);
			float attack = (float) maid.getAttributeValue(Attributes.ATTACK_DAMAGE);
			if (projectile instanceof DanmakuBulletEntity bullet) {
				bullet.damage = bullet.damage * multiplier + attack;
			} else if (projectile instanceof DanmakuLaserEntity laser) {
				laser.damage = laser.damage * multiplier + attack;
			}
		}
	}

}
