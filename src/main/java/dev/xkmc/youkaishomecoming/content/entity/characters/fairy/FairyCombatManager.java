package dev.xkmc.youkaishomecoming.content.entity.characters.fairy;

import dev.xkmc.youkaishomecoming.content.entity.behavior.combat.DefaultCombatManager;
import dev.xkmc.youkaishomecoming.content.entity.behavior.combat.TargetKind;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

class FairyCombatManager extends DefaultCombatManager {

	private static final ResourceLocation SPELL_RUMIA = GensokyoLegacy.loc("rumia");

	public FairyCombatManager(GeneralYoukaiEntity e) {
		super(e, SPELL_RUMIA);
	}

	@Override
	public TargetKind targetKind(LivingEntity le) {
		// Fairies also attack YOUKAIFIED players (in addition to YOUKAIFYING handled by parent)
		if (le instanceof Player && le.hasEffect(YHEffects.YOUKAIFIED)) {
			return TargetKind.ENEMY;
		}
		return super.targetKind(le);
	}

	@Override
	public boolean shouldHurtInnocent(LivingEntity le) {
		return le instanceof Mob mob && mob.getTarget() != null;
	}

}
