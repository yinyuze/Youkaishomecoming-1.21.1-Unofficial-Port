package dev.xkmc.youkaishomecoming.content.block.plant;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WildVineBlock extends BushBlock {

	public static final MapCodec<WildVineBlock> CODEC = simpleCodec(WildVineBlock::new);

	public WildVineBlock(Properties prop) {
		super(prop);
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.above()).is(BlockTags.LEAVES);
	}

}
