package dev.xkmc.youkaishomecoming.content.item.character;

import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;

public class TouhouHatItemClient {

	public static boolean canShowTooltip() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return false;
		if (player.isCreative()) return true;
		return isCharacter(player);
	}

	private static boolean isCharacter(LivingEntity e) {
		return e instanceof YoukaiEntity
				|| e.hasEffect(YHEffects.YOUKAIFYING)
				|| e.hasEffect(YHEffects.YOUKAIFIED)
				|| e.hasEffect(YHEffects.FAIRY);
	}
}
