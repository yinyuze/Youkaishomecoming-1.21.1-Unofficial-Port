package dev.xkmc.youkaishomecoming.content.client.structure;

import dev.xkmc.youkaishomecoming.content.attachment.home.core.IHomeHolder;
import dev.xkmc.youkaishomecoming.content.attachment.index.StructureKey;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record StructureInfoRequestToServer(
		StructureKey key
) implements SerialPacketBase<StructureInfoRequestToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var home = IHomeHolder.of(sp.serverLevel(), key);
		if (home == null || !home.isValid()) return;
		GensokyoLegacy.HANDLER.toClientPlayer(home.getUpdatePacket(), sp);
	}

}
