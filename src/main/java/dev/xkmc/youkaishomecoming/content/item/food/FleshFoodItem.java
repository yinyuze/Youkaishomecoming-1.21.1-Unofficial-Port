package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.event.ReimuEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleshFoodItem extends YHFoodItem {

	public FleshFoodItem(Properties props) {
		super(props);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		FoodProperties old = super.getFoodProperties(stack, entity);
		if (old == null) return null;
		int factor = 1;
		if (entity != null) {
			if (entity.hasEffect(YHEffects.YOUKAIFIED)) factor = 3;
			else if (entity.hasEffect(YHEffects.YOUKAIFYING)) factor = 2;
		}
		if (factor == 1) return old;
		float satMod = old.nutrition() > 0 ? old.saturation() / (old.nutrition() * 2.0f) : 0;
		var builder = new FoodProperties.Builder();
		builder.nutrition(old.nutrition() * factor);
		builder.saturationModifier(satMod);
		if (old.canAlwaysEat()) builder.alwaysEdible();
		for (var ent : old.effects()) {
			builder.effect(ent::effect, ent.probability());
		}
		return builder.build();
	}

	@Override
	public Component getName(ItemStack stack) {
		Component fleshName;
		if (FMLEnvironment.dist == Dist.CLIENT) {
			fleshName = FleshFoodItemClient.getFleshName();
		} else {
			fleshName = GLLang.FLESH$FLESH_HUMAN.get();
		}
		return Component.translatable(getDescriptionId(stack), fleshName);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, list, flag);
		if (FMLEnvironment.dist == Dist.CLIENT) {
			FleshFoodItemClient.appendTasteTooltip(list);
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
		ItemStack ans = super.finishUsingItem(stack, level, consumer);
		if (consumer instanceof Player player && !level.isClientSide()) {
			consume(player);
		}
		return ans;
	}

	private void consume(Player player) {
		if (player.hasEffect(YHEffects.YOUKAIFIED)) {
			var eff = player.getEffect(YHEffects.YOUKAIFIED);
			if (eff != null) {
				int dur = eff.getDuration() + YHModConfig.SERVER.youkaifiedProlongation.get();
				player.addEffect(new MobEffectInstance(YHEffects.YOUKAIFIED, dur, 0, true, false, true));
			}
		} else if (player.hasEffect(YHEffects.YOUKAIFYING)) {
			var eff = player.getEffect(YHEffects.YOUKAIFYING);
			if (eff != null) {
				int dur = eff.getDuration() + YHModConfig.SERVER.youkaifyingTime.get();
				if (dur > YHModConfig.SERVER.youkaifyingThreshold.get()) {
					dur = YHModConfig.SERVER.youkaifiedDuration.get();
					player.removeEffect(YHEffects.YOUKAIFYING);
					player.addEffect(new MobEffectInstance(YHEffects.YOUKAIFIED, dur, 0, true, false, true));
				} else {
					player.addEffect(new MobEffectInstance(YHEffects.YOUKAIFYING, dur, 0, false, false, false));
				}
			}
		} else {
			if (player.getRandom().nextDouble() < YHModConfig.SERVER.youkaifyingChance.get() / 100.0) {
				int dur = YHModConfig.SERVER.youkaifyingTime.get();
				player.addEffect(new MobEffectInstance(YHEffects.YOUKAIFYING, dur, 0, false, false, false));
				dur = YHModConfig.SERVER.youkaifyingConfusionTime.get();
				player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, dur, 0, false, false, true));
			}
		}
		if (getDefaultInstance().is(YHTagGen.APPARENT_FLESH_FOOD) && player instanceof net.minecraft.server.level.ServerPlayer sp) {
			ReimuEventHandlers.triggerReimuResponse(sp, 24, true);
		}
	}

}
