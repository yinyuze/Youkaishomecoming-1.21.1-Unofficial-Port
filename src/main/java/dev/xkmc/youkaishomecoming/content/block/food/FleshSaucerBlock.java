package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItemClient;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.food.Saucer;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class FleshSaucerBlock extends BaseSaucerBlock {

	private final VoxelShape shape;

	public FleshSaucerBlock(Properties props, int height) {
		super(props);
		this.shape = Block.box(2.0, 0.0, 2.0, 14.0, height, 14.0);
	}

	@Override
	public MutableComponent getName() {
		Component name;
		if (FMLEnvironment.dist == Dist.CLIENT) {
			name = FleshFoodItemClient.getFleshName();
		} else {
			name = GLLang.FLESH$FLESH_HUMAN.get();
		}
		return Component.translatable(getDescriptionId(), name);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (player.canEat(false)) {
			if (!level.isClientSide()) {
				player.eat(level, asItem().getDefaultInstance());
				level.setBlockAndUpdate(pos, YHItems.SAUCER.getDefaultState()
						.setValue(EmptySaucerBlock.TYPE, Saucer.SAUCER_1)
						.setValue(BlockStateProperties.HORIZONTAL_FACING,
								state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
			}
			return InteractionResult.CONSUME;
		}
		return super.useWithoutItem(state, level, pos, player, hit);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return shape;
	}
}
