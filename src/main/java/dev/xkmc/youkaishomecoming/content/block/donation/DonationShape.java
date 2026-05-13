package dev.xkmc.youkaishomecoming.content.block.donation;

import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.one.EntityInsideBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DonationShape implements ShapeBlockMethod, EntityInsideBlockMethod, UseItemOnBlockMethod {

	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 15, 16);

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity item) {
			if (level.getBlockEntity(pos) instanceof DonationBoxBlockEntity be) {
				Player player = item.getOwner() instanceof Player pl ? pl : null;
				be.take(player, item.getItem());
				if (item.getItem().isEmpty())
					item.discard();
			}
		}
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!stack.is(Items.EMERALD) && !stack.is(Items.GOLD_INGOT)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!(level.getBlockEntity(pos) instanceof DonationBoxBlockEntity be)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!level.isClientSide()) {
			ItemStack single = stack.copyWithCount(1);
			be.take(player, single);
			if (single.isEmpty()) {
				stack.shrink(1);
			}
		}
		return ItemInteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

}
