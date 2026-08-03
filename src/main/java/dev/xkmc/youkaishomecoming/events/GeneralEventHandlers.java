package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.block.combined.CombinedBlockSet;
import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GeneralEventHandlers {

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getItemStack().is(Items.DEBUG_STICK)) return;
		var level = event.getLevel();
		var pos = event.getPos();
		var state = level.getBlockState(pos);
		LeftClickBlock block = null;
		if (state.getBlock() instanceof LeftClickBlock b) {
			block = b;
		} else if (level.getBlockEntity(pos) instanceof LeftClickBlock b) {
			block = b;
		}
		if (block == null) return;
		if (block.leftClick(state, level, pos, event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		CombinedBlockSet.onRightClickBlock(event);
		if (event.getLevel().isClientSide()) return;
		if (event.getEntity() instanceof net.minecraft.world.entity.player.Player player
				&& player.hasEffect(dev.xkmc.youkaishomecoming.init.registrate.GLEffects.UNCONSCIOUS)) {
			var state = event.getLevel().getBlockState(event.getPos());
			String className = state.getBlock().getClass().getName();
			if (className.contains(".lootr.") || className.contains("Lootr")) {
				dev.xkmc.youkaishomecoming.event.KoishiEventHandlers.removeKoishi(player);
				return;
			}
			var be = event.getLevel().getBlockEntity(event.getPos());
			if (be instanceof net.minecraft.world.RandomizableContainer rc && rc.getLootTable() != null) {
				dev.xkmc.youkaishomecoming.event.KoishiEventHandlers.removeKoishi(player);
			}
		}
	}

	@SubscribeEvent
	public static void onPhantomSpawn(PlayerSpawnPhantomsEvent event) {
		if (event.getEntity().hasEffect(YHEffects.SOBER)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
		if (dev.xkmc.youkaishomecoming.compat.curios.CuriosManager.hasHead(
				event.getEntity(), dev.xkmc.youkaishomecoming.init.registrate.YHItems.CAMELLIA.get(), false)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}

	public static boolean supressVibration(Entity self) {
		if (self instanceof TraceableEntity item) {
			if (item.getOwner() instanceof LivingEntity le) {
				self = le;
			}
		}
		if (self instanceof LivingEntity le) {
			return le.hasEffect(YHEffects.UDUMBARA);
		}
		return false;
	}

}
