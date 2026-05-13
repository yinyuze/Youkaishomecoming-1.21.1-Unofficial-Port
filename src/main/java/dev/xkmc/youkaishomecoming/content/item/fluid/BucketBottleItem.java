package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

public class BucketBottleItem extends BlockItem {

	protected final IYHFluidBottled fluid;

	public BucketBottleItem(Block block, Properties properties, IYHFluidBottled fluid) {
		super(block, properties);
		this.fluid = fluid;
	}

	@Override
	public InteractionResult place(BlockPlaceContext ctx) {
		if (ctx.getPlayer() != null && !ctx.getPlayer().isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		return super.place(ctx);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
		var food = getFoodProperties(stack, player);
		if (food == null || food == SlipBottleItem.NONE || food.effects().isEmpty())
			return InteractionResultHolder.pass(stack);
		// Start drinking animation instead of placing block
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("BucketBottleItem.finishUsingItem called - stack: {}, user: {}, isClientSide: {}", stack, user, level.isClientSide);

		var other = stack.getCount() == 1 ? ItemStack.EMPTY : stack.split(stack.getCount() - 1);
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Handler: {}, instanceof SlipFluidWrapper: {}", handler, handler instanceof SlipFluidWrapper);

		if (handler == null || !(handler instanceof SlipFluidWrapper slip)) return stack;
		var fluid = slip.getFluid();
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Fluid before drain: {}", fluid);

		// Apply food effects manually
		var food = getFoodProperties(stack, user);
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Food properties: {}", food);

		if (food != null && food != SlipBottleItem.NONE) {
			// Apply nutrition and saturation
			if (user instanceof Player player) {
				player.getFoodData().eat(food.nutrition(), food.saturation());
				dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Applied nutrition: {}, saturation: {}", food.nutrition(), food.saturation());
			}
			// Apply effects
			if (!level.isClientSide) {
				for (var effect : food.effects()) {
					if (level.random.nextFloat() < effect.probability()) {
						user.addEffect(new net.minecraft.world.effect.MobEffectInstance(effect.effect()));
						dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Applied effect: {}", effect.effect());
					}
				}
			}
		}

		// Drain fluid - this will automatically update the container
		slip.getContainer().setCount(1);
		var drained = slip.drain(50, IFluidHandler.FluidAction.EXECUTE);
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Drained: {}, fluid after drain: {}", drained, slip.getFluid());

		var result = slip.getContainer();
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Container before return: {}, DC_FLUID: {}", result, result.get(dev.xkmc.youkaishomecoming.init.registrate.YHItems.DC_FLUID));

		if (!other.isEmpty()) {
			// stack.getOrCreateTag().putString("BottleMarker", "DISCARD"); // 1.21.1 uses DataComponents
			if (user instanceof Player player) {
				player.getInventory().placeItemBackInInventory(other);
			} else {
				user.spawnAtLocation(other);
			}
			stack.setCount(0);
		}
		dev.xkmc.youkaishomecoming.init.GensokyoLegacy.LOGGER.info("Returning: {}", result);
		return result;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.PLACE.get());
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			YHFoodItem.getFoodEffects(stack, list);
		super.appendHoverText(stack, level, list, flag);
	}

	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var food = fluid.asStack(1).getFoodProperties(entity);
		if (food == null || food == SlipBottleItem.NONE) return SlipBottleItem.NONE;
		var builder = new FoodProperties.Builder();
		if (food.canAlwaysEat()) builder.alwaysEdible();
		for (var e : food.effects()) {
			var ins = e.effect();
			var ans = new EffectBuilder(ins);
			if (ins.getEffect() == YHEffects.DRUNK.get()) {
				int amp = ins.getAmplifier() + 1;
				ans.setDuration(amp * ins.getDuration() / 5);
				ans.setAmplifier(0);
			} else {
				ans.setDuration(ins.getDuration() / 5);
			}
			builder.effect(() -> ans.ins, e.probability());
		}
		return builder.build();

	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 10;
	}

	@Override
	public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack stack) {
		return net.minecraft.world.item.UseAnim.DRINK;
	}

}
