package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class FairyEffect extends MobEffect {

	public FairyEffect(MobEffectCategory category, int color) {
		super(category, color);
		var uuid = GensokyoLegacy.loc("fairy");
		addAttributeModifier(Attributes.MAX_HEALTH, uuid, -0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.ATTACK_DAMAGE, GensokyoLegacy.loc("fairy_atk"), -0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, GensokyoLegacy.loc("fairy_spd"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		// not curable by milk
	}

}
