package dev.xkmc.youkaishomecoming.content.entity.behavior.task.core;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.List;

public class YoukaiVanishTask extends ExtendedBehaviour<SmartYoukaiEntity> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(2)
			.noMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET);

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, SmartYoukaiEntity entity) {
		return false;
	}

	@Override
	protected void start(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
	}

}
