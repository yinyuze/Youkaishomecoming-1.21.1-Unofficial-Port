package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.init.data.GLLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class FleshBlockItem extends BlockItem {

	public FleshBlockItem(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public Component getName(ItemStack stack) {
		Component fleshName = FMLEnvironment.dist == Dist.CLIENT
				? FleshFoodItemClient.getFleshName()
				: GLLang.FLESH$FLESH_HUMAN.get();
		return Component.translatable(getDescriptionId(stack), fleshName);
	}
}
