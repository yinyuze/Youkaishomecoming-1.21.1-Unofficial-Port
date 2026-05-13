package dev.xkmc.youkaishomecoming.content.client.debug;

import dev.xkmc.youkaishomecoming.content.block.base.IDebugInfoBlockEntity;
import dev.xkmc.youkaishomecoming.content.client.structure.StructureInfoClientManager;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;

public class DebugOverlay implements LayeredDraw.Layer {

	@Override
	public void render(GuiGraphics g, DeltaTracker delta) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var level = player.level();
		var hit = Minecraft.getInstance().hitResult;
		if (!player.getItemBySlot(EquipmentSlot.HEAD).is(GLItems.DEBUG_GLASSES)) return;
		List<Component> lines = new ArrayList<>();
		long time = player.level().getGameTime();
		if (hit instanceof BlockHitResult block) {
			if (level.getBlockEntity(block.getBlockPos()) instanceof IDebugInfoBlockEntity bed) {
				BedInfoClientManager.tooltip(lines, time, bed);
			} else {
				StructureInfoClientManager.tooltip(lines, time, block.getBlockPos());
			}
		} else if (hit instanceof EntityHitResult ehit) {
			if (ehit.getEntity() instanceof YoukaiEntity youkai) {
				CharacterInfoClientManager.tooltip(lines, time, youkai);
			}
		}
		if (lines.isEmpty()) return;
		int sw = g.guiWidth();
		int sh = g.guiHeight();
		new OverlayUtil(g, (int) (sw * 0.6), (int) (sh * 0.5), (int) (sw * 0.3))
				.renderLongText(Minecraft.getInstance().font, lines);
	}

}
