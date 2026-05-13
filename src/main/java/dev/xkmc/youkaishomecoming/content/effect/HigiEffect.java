package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class HigiEffect extends MobEffect {

	public HigiEffect(MobEffectCategory category, int color) {
		super(category, color);
		var uuid = GensokyoLegacy.loc("higi");
		addAttributeModifier(Attributes.ATTACK_DAMAGE, uuid, 0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, GensokyoLegacy.loc("higi_spd"), 0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int lv) {
		if (e.getHealth() < e.getMaxHealth()) {
			int period = GLModConfig.SERVER.higiHealingPeriod.get() >> lv;
			if (e.hasEffect(YHEffects.FAIRY)) {
				period /= 3;
			}
			if (e.tickCount % Math.max(1, period) == 0) {
				e.heal(1);
			}
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		// not curable by milk
	}

}
