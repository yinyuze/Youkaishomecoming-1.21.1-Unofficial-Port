package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

/**
 * Large flask item that holds 20 uses (1000mb) instead of 4 uses (200mb)
 * Used for soy sauce and mayonnaise to save resources
 */
public class FlaskItem extends Item {

	private final IYHFluidBottled fluid;

	public FlaskItem(Properties properties, IYHFluidBottled fluid) {
		super(properties);
		this.fluid = fluid;
		SlipFluidWrapper.add(this);
	}

	@Override
	public Component getName(ItemStack stack) {
		var fluidStack = getFluid(stack);
		if (fluidStack.isEmpty()) return super.getName(stack);
		return YHLangData.FLASK_OF.get(fluidStack.getHoverName());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var fluidStack = getFluid(stack);
		if (!fluidStack.isEmpty()) {
			list.add(YHLangData.FLASK_CONTENT.get(fluidStack.getHoverName().copy().withStyle(ChatFormatting.WHITE)));
			int amount = fluidStack.getAmount();
			if (amount % 50 == 0 && amount > 0 && amount <= 1000) {
				list.add(YHLangData.FLASK_USE.get(amount / 50, 20));
			}
			list.add(YHLangData.FLASK_INFO_SAUCE.get());
		} else {
			list.add(YHLangData.FLASK_INFO_SAUCE.get());
		}
		super.appendHoverText(stack, level, list, flag);
	}

	public static FluidStack getFluid(ItemStack stack) {
		var cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
		return cap == null ? FluidStack.EMPTY : cap.getFluidInTank(0);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return !getFluid(stack).isEmpty();
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return 13 * getFluid(stack).getAmount() / 1000;
	}

	public int getBarColor(ItemStack stack) {
		return -1;
	}

	public static int color(ItemStack stack, int layer) {
		if (layer != 1) return -1;
		var fluid = getFluid(stack);
		if (fluid.isEmpty()) return -1;
		if (net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT)
			return FluidColorHelper.getColor(fluid);
		return -1;
	}

	public static float texture(ItemStack stack) {
		var fluid = getFluid(stack);
		if (fluid.isEmpty()) return 0;
		if (!(YHFluidHandler.of(fluid) instanceof IYHFluidBottled liquid)) return 0;
		var set = liquid.bottleSet();
		if (set == null) return 0;
		return (set.index + 1) * 1f / BottleTexture.getListSize();
	}
}
