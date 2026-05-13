package dev.xkmc.youkaishomecoming.compat.curios;

import dev.xkmc.youkaishomecoming.init.data.GLTagGen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

public class CuriosManager {

	public static boolean hasWings(LivingEntity le, Item item, boolean checkRender) {
		if (le.getItemBySlot(EquipmentSlot.CHEST).is(item))
			return true;
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.hasWings(le, item, checkRender);
		}
		return false;
	}

	public static boolean hasAnyWings(LivingEntity le) {
		if (le.getItemBySlot(EquipmentSlot.CHEST).is(GLTagGen.TOUHOU_WINGS))
			return true;
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.hasAnyWings(le);
		}
		return false;
	}

}
