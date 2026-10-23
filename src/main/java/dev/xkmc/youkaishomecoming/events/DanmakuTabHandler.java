package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.danmakuapi.init.DanmakuAPI;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DanmakuTabHandler {

	@SubscribeEvent
	public static void onBuildTab(BuildCreativeModeTabContentsEvent event) {
		if (!event.getTabKey().equals(GensokyoLegacy.DANMAKU_TAB.key())) return;
		BuiltInRegistries.ITEM.entrySet().stream()
				.filter(e -> e.getKey().location().getNamespace().equals(DanmakuAPI.MODID))
				.map(e -> new ItemStack(e.getValue()))
				// Danmaku API already contributes these items to the global search results.
				// Adding them to search again makes NeoForge reject the duplicate stack.
				.forEach(stack -> event.accept(stack, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY));
	}

}
