package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class FleshFoodItemClient {

	private FleshFoodItemClient() {
	}

	public static Component getFleshName() {
		Player player = Minecraft.getInstance().player;
		if (player != null && player.hasEffect(YHEffects.YOUKAIFIED)) {
			return GLLang.FLESH$FLESH_YOUKAI.get();
		}
		return GLLang.FLESH$FLESH_HUMAN.get();
	}

	public static void appendTasteTooltip(List<Component> list) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		if (player.hasEffect(YHEffects.YOUKAIFIED)) {
			list.add(GLLang.FLESH$TASTE_YOUKAI.get());
		} else if (player.hasEffect(YHEffects.YOUKAIFYING)) {
			list.add(GLLang.FLESH$TASTE_HALF_YOUKAI.get());
		} else {
			list.add(GLLang.FLESH$TASTE_HUMAN.get());
		}
	}
}
