package dev.xkmc.youkaishomecoming.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.xkmc.youkaishomecoming.content.attachment.misc.FrogGodCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.FrogAttackablesSensor;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrogAttackablesSensor.class)
public class FrogAttackablesSensorMixin {

	@ModifyReturnValue(method = "isMatchingEntity", at = @At("RETURN"))
	private boolean youkaishomecoming$addFrogTargets(boolean original, LivingEntity attacker, LivingEntity target) {
		if (original) return true;
		if (target != null && attacker instanceof Frog frog && FrogGodCapability.canEatSpecial(frog, target)) {
			return true;
		}
		return false;
	}

}
