package dev.xkmc.youkaishomecoming.content.entity.characters.fairy;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.behavior.combat.TargetKind;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

class CirnoCombatManager extends FairyCombatManager {

	public CirnoCombatManager(CirnoEntity cirnoEntity) {
		super(cirnoEntity);
	}

	@Override
	public TargetKind targetKind(LivingEntity le) {
		if (le instanceof Frog) return TargetKind.PRAY;
		return super.targetKind(le);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source.is(DamageTypeTags.IS_FREEZING);
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		if (targetKind(e).noAdditionalEffect()) return;
		e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
		if (e.canFreeze()) {
			e.setTicksFrozen(Math.min(200, e.getTicksFrozen() + 120));
		}
	}

	@Override
	public int doPreyAttack(LivingEntity target) {
		double dx = target.getX() - self.getX();
		double dy = target.getY(0.5D) - self.getEyeY();
		double dz = target.getZ() - self.getZ();
		Vec3 vec = new Vec3(dx, dy, dz).normalize().scale(0.6);
		float dmg = (float) self.getAttributeValue(Attributes.ATTACK_DAMAGE);
		self.shoot(dmg, 40, vec, DyeColor.CYAN);
		return 20;
	}

}
