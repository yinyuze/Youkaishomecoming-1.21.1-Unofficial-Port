package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class YHGeneralEvents {

	@SubscribeEvent
	public static void onBrewingGen(RegisterBrewingRecipesEvent event) {
		YHEffects.registerBrewingRecipe(event);
	}

	@SubscribeEvent
	public static void onSleep(CanPlayerSleepEvent event) {
		if (event.getEntity().hasEffect(YHEffects.SOBER)) {
			event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHeal(LivingHealEvent event) {
		float amount = event.getAmount();
		if (event.getEntity().hasEffect(YHEffects.SMOOTHING)) {
			amount *= YHModConfig.SERVER.smoothingHealingFactor.get();
		}
		if (event.getEntity().hasEffect(YHEffects.FAIRY)) {
			amount *= GLModConfig.SERVER.fairyHealingFactor.get();
		}
		event.setAmount(amount);
	}

	@SubscribeEvent
	public static void onTick(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof LivingEntity e)) return;
		if (e.hasEffect(YHEffects.THICK) && e.hasEffect(MobEffects.WITHER)) {
			e.removeEffect(MobEffects.WITHER);
		}
		if (e.hasEffect(YHEffects.SMOOTHING) && e.hasEffect(MobEffects.POISON)) {
			e.removeEffect(MobEffects.POISON);
		}
		if (e.hasEffect(YHEffects.REFRESHING) && e.isOnFire()) {
			e.clearFire();
		}
	}

	@SubscribeEvent
	public static void onEffectTest(MobEffectEvent.Applicable event) {
		var ins = event.getEffectInstance();
		if (ins == null) return;
		if (ins.is(MobEffects.WITHER)) {
			if (event.getEntity().hasEffect(YHEffects.THICK)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
		if (ins.is(MobEffects.POISON)) {
			if (event.getEntity().hasEffect(YHEffects.SMOOTHING)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
		if (ins.is(YHEffects.YOUKAIFYING)) {
			if (event.getEntity().hasEffect(YHEffects.SOBER) ||
					event.getEntity().hasEffect(YHEffects.FAIRY) ||
					event.getEntity().hasEffect(YHEffects.YOUKAIFIED)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
		if (ins.is(YHEffects.YOUKAIFIED)) {
			if (event.getEntity().hasEffect(YHEffects.SOBER) ||
					event.getEntity().hasEffect(YHEffects.FAIRY)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
		if (ins.is(YHEffects.FAIRY)) {
			if (event.getEntity().hasEffect(YHEffects.YOUKAIFYING) ||
					event.getEntity().hasEffect(YHEffects.YOUKAIFIED)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
	}

}
