package dev.xkmc.youkaishomecoming.content.block.base;

import dev.xkmc.youkaishomecoming.content.client.debug.BlockInfoToClient;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IDebugInfoBlockEntity {

	default BlockEntity asBlockEntity() {
		return Wrappers.cast(this);
	}

	BlockInfoToClient getDebugPacket(ServerPlayer player);

}
