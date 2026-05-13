package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickRemoveLidMethod implements UseWithoutItemBlockMethod {

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(player.getUsedItemHand());
		if (state.is(YHBlocks.STEAMER_LID.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
			}
		} else {
			if (RackInfo.ofY(hit) != RackInfo.getRackInfo(state).height() || !RackInfo.tryRemoveCap(level, pos, state))
				return InteractionResult.PASS;
		}
		if (!level.isClientSide()) {
			if (stack.isEmpty() && !player.isShiftKeyDown()) {
				player.setItemInHand(player.getUsedItemHand(), YHBlocks.STEAMER_LID.asStack());
			} else {
				player.getInventory().placeItemBackInInventory(YHBlocks.STEAMER_LID.asStack());
			}
		}
		return InteractionResult.SUCCESS;
	}

}
