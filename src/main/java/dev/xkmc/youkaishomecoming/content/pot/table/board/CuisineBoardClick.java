package dev.xkmc.youkaishomecoming.content.pot.table.board;

import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import dev.xkmc.youkaishomecoming.content.pot.table.item.SearHelper;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class CuisineBoardClick implements UseItemOnBlockMethod, UseWithoutItemBlockMethod {

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof CuisineBoardBlockEntity be) {
			if (player.isShiftKeyDown()) {
				be.clear();
				player.playSound(SoundEvents.COMPOSTER_FILL, 1, 1);
				return InteractionResult.SUCCESS;
			}
			if (be.addToPlayer(player)) {
				return InteractionResult.SUCCESS;
			}
			int cost = be.addItem(ItemStack.EMPTY);
			if (cost > 0) {
				if (!level.isClientSide && player instanceof ServerPlayer sp && be.getModel().complete(level).isPresent()) {
					YHCriteriaTriggers.TABLE.get().trigger(sp);
				}
				player.playSound(SoundEvents.WOOL_PLACE, 1, 1);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof CuisineBoardBlockEntity be) {
			if (player.isShiftKeyDown()) {
				be.clear();
				player.playSound(SoundEvents.COMPOSTER_FILL, 1, 1);
				return ItemInteractionResult.SUCCESS;
			}
			if (be.performToolAction(stack)) {
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (stack.isDamageableItem()) {
						stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
					}
				}
				player.playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
				return ItemInteractionResult.SUCCESS;
			}
			int cost = be.addItem(stack);
			if (cost > 0) {
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (SearHelper.isFireSource(stack)) {
						if (stack.isDamageableItem()) {
							stack.hurtAndBreak(cost, player, LivingEntity.getSlotForHand(hand));
							return ItemInteractionResult.SUCCESS;
						}
					}
					// TODO: SlipBottleItem disabled - needs NeoForge 1.21.1 capability rewrite
					// if (SlipBottleItem.isSlipContainer(stack)) {
					//     var toConsume = stack.split(1);
					//     player.setItemInHand(hand, SlipBottleItem.drain(toConsume));
					//     player.getInventory().placeItemBackInInventory(stack);
					//     return ItemInteractionResult.SUCCESS;
					// }
					boolean withCont = stack.is(YHTagGen.PLACE_WITH_CONTAINER);
					ItemStack cont = stack.getCraftingRemainingItem();
					stack.shrink(cost);
					if (!withCont && !cont.isEmpty()) {
						player.getInventory().placeItemBackInInventory(cont.copyWithCount(cost));
					}
					if (player instanceof ServerPlayer sp && be.getModel().complete(level).isPresent()) {
						YHCriteriaTriggers.TABLE.get().trigger(sp);
					}
				}
				player.playSound(SoundEvents.WOOL_PLACE, 1, 1);
				return ItemInteractionResult.SUCCESS;
			}
			if (stack.isEmpty() && be.addToPlayer(player)) {
				return ItemInteractionResult.SUCCESS;
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

}
