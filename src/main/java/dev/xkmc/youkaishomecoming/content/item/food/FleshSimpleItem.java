package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.init.data.GLLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class FleshSimpleItem extends Item {

	public FleshSimpleItem(Properties props) {
		super(props);
	}

	@Override
	public Component getName(ItemStack stack) {
		Component fleshName = FMLEnvironment.dist == Dist.CLIENT
				? FleshFoodItemClient.getFleshName()
				: GLLang.FLESH$FLESH_HUMAN.get();
		return Component.translatable(getDescriptionId(stack), fleshName);
	}
}
