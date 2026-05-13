package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class EarthyEffect extends MobEffect {

	public EarthyEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int amp) {
		if (!(e instanceof Player player)) return true;
		var food = player.getFoodData();
		if (food.getFoodLevel() > 18) {
			if (e.getHealth() < e.getMaxHealth()) {
				e.heal(1);
				food.addExhaustion(4);
			}
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % Math.max(1, 10 / (amplifier + 1)) == 0;
	}

	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		// not curable by milk
	}

}
