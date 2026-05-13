package dev.xkmc.youkaishomecoming.event;

import dev.xkmc.youkaishomecoming.content.entity.characters.maiden.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.GLAdvGen;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.GLCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.GLEntities;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReimuEventHandlers {

	private static List<Villager> getWitness(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return List.of();
		AABB aabb = le.getBoundingBox().inflate(range);
		return sl.getEntities(EntityTypeTest.forClass(Villager.class), aabb,
				e -> e.isAlive() && !e.hasEffect(YHEffects.HYPNOSIS)
						&& (!eatFlesh || e.hasLineOfSight(le)));
	}

	public static void triggerReimuResponse(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return;
		if (!le.level().dimension().equals(Level.OVERWORLD)) return;
		var list = getWitness(le, range, eatFlesh);
		if (!list.isEmpty()) {
			if (le instanceof ServerPlayer sp && eatFlesh) {
				if (!GLModConfig.SERVER.reimuSummonFlesh.get() || fleshWarn(sp)) {
					for (var e : list) {
						sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
						sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
					}
					return;
				}
			}
			if (trySummonMaidenAttack(sl, le)) {
				list.forEach(GolemSensor::golemDetected);
			}
		}
		for (var e : list) {
			sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
			sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
		}
	}

	public static boolean koishiBlockReimu(LivingEntity le) {
		var hat = GLItems.KOISHI_HAT.get();
		if (le instanceof ServerPlayer sp &&
				le.getItemBySlot(EquipmentSlot.HEAD).is(hat) &&
				!sp.getCooldowns().isOnCooldown(hat)) {
			KoishiEventHandlers.removeKoishi(le);
			sp.getCooldowns().addCooldown(hat, 1200);
			sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
			sp.sendSystemMessage(GLLang.MSG$KOISHI_REIMU.get(), true);
			return true;
		}
		return false;
	}

	public static boolean fleshWarn(ServerPlayer sp) {
		if (koishiBlockReimu(sp)) return true;
		var adv = sp.server.getAdvancements().get(GLAdvGen.FLESH_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return false;
		GLCriteriaTriggers.FLESH_WARN.get().trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(GLLang.MSG$REIMU_FLESH.get(), true);
		return true;
	}

	public static void hurtWarn(ServerPlayer sp) {
		if (!sp.level().dimension().equals(Level.OVERWORLD)) return;
		if (getWitness(sp, 16, false).isEmpty()) return;
		if (koishiBlockReimu(sp)) return;
		var adv = sp.server.getAdvancements().get(GLAdvGen.HURT_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return;
		GLCriteriaTriggers.HURT_WARN.get().trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(GLLang.MSG$REIMU_WARN.get(), true);
	}

	@Nullable
	public static MaidenEntity trySummonMaiden(ServerLevel sl, LivingEntity le) {
		com.tterrag.registrate.util.entry.EntityEntry<? extends MaidenEntity> type = GLEntities.REIMU;
		float r = le.getRandom().nextFloat();
		if (r < 0.333f) {
			type = GLEntities.SANAE;
		} else if (r < 0.667f) {
			type = GLEntities.MARISA;
		}
		var e = trySummon(type, sl, le, 5, 5);
		if (e == null) return null;
		e.initSpellCard();
		sl.addFreshEntity(e);
		return e;
	}

	@Nullable
	public static <T extends YoukaiEntity> T trySummon(com.tterrag.registrate.util.entry.EntityEntry<T> type, ServerLevel sl, LivingEntity le, int y0, int dy) {
		BlockPos center = BlockPos.containing(le.position().add(le.getForward().scale(8)).add(0, y0, 0));
		T e = type.create(sl);
		if (e == null) return null;
		if (!setRandomizedPos(le, e, center, dy)) return null;
		return e;
	}

	private static boolean trySummonMaidenAttack(ServerLevel sl, LivingEntity le) {
		if (le instanceof ServerPlayer sp && sp.isCreative()) return false;
		if (koishiBlockReimu(le)) return false;
		KoishiEventHandlers.removeKoishi(le);
		var list = sl.getEntities(EntityTypeTest.forClass(MaidenEntity.class),
				le.getBoundingBox().inflate(32), LivingEntity::isAlive);
		if (!list.isEmpty()) {
			for (var e : list) {
				e.setTarget(le);
				e.refreshIdle();
			}
			return false;
		}
		var maiden = trySummonMaiden(sl, le);
		if (maiden == null) return false;
		KoishiEventHandlers.removeKoishi(le);
		maiden.setTarget(le);
		return true;
	}

	public static boolean setRandomizedPos(LivingEntity target, YoukaiEntity youkai, BlockPos center, int dy) {
		BlockPos pos = getPosForSpawn(target, youkai, center, 16, 8, dy);
		if (pos == null) {
			center = target.blockPosition().above(5);
			pos = getPosForSpawn(target, youkai, center, 16, 16, dy);
		}
		if (pos == null) return false;
		youkai.moveTo(pos, 0, 0);
		return true;
	}

	@Nullable
	public static BlockPos getPosForSpawn(LivingEntity target, YoukaiEntity youkai, BlockPos center, int trial, int range, int dy) {
		for (int i = 0; i < trial; i++) {
			BlockPos pos = center.offset(
					target.getRandom().nextInt(-range, range),
					target.getRandom().nextInt(-dy, dy),
					target.getRandom().nextInt(-range, range)
			);
			youkai.moveTo(pos, 0, 0);
			if (target.level().noCollision(youkai) && youkai.hasLineOfSight(target)) {
				return pos;
			}
		}
		return null;
	}

}
