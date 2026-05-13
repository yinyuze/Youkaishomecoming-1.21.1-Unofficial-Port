package dev.xkmc.youkaishomecoming.init.data.structure;

import dev.xkmc.youkaishomecoming.content.attachment.datamap.CharacterConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public record StructBed(
		Holder<EntityType<?>> entity,
		CharacterConfig data,
		Holder<Block>... bed
) {
	@SafeVarargs
	public StructBed {
	}
}
