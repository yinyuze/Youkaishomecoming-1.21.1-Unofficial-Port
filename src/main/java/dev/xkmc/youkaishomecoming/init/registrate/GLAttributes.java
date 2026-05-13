package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class GLAttributes {

	private static final SR<Attribute> REG = SR.of(GensokyoLegacy.REG, BuiltInRegistries.ATTRIBUTE);

	public static final Val<Attribute> MAX_RESOURCE = reg("max_resource", 0, -10, 10);
	public static final Val<Attribute> MAX_POWER = reg("max_power", 0, -10, 10);
	public static final Val<Attribute> INITIAL_RESOURCE = reg("initial_resource", 0, -10, 10);
	public static final Val<Attribute> INITIAL_POWER = reg("initial_power", 0, -10, 10);
	public static final Val<Attribute> GRAZE_EFFECTIVENESS = reg("graze_effectiveness", 1, 0, 10);
	public static final Val<Attribute> HITBOX = reg("hit_box", 0, -0.2, 1);

	private static Val<Attribute> reg(String id, double def, double min, double max) {
		return REG.reg(id, () -> new RangedAttribute("attribute." + GensokyoLegacy.MODID + "." + id, def, min, max).setSyncable(true));
	}

	public static void register() {

	}

}
