package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickTakeItemMethod implements UseWithoutItemBlockMethod {

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		RackInfo info = RackInfo.getRackInfo(state);
		if (info.racks() == 0) return InteractionResult.PASS;
		if (!(level.getBlockEntity(pos) instanceof SteamerBlockEntity be)) return InteractionResult.PASS;
		int y = RackInfo.ofY(hit);
		if (info.pot()) y -= 2;
		if (y < 0 || y >= be.racks.size()) return InteractionResult.PASS;
		var rack = be.racks.get(y);
		if (hit.getDirection() == Direction.UP && !RackInfo.isCapped(level, pos)) {
			if (rack.tryTakeItemAt(be, level, hit.getLocation(), player, player.getUsedItemHand())) {
				return InteractionResult.SUCCESS;
			}
			if (!player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}
		}
		if (rack.tryTakeItem(be, level, player, player.getUsedItemHand())) {
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
