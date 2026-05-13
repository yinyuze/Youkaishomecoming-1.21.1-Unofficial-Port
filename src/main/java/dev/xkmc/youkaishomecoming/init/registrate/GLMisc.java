package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.loot.KilledByRumiaHairbandCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class GLMisc {

    private static final SR<LootItemConditionType> LIC = SR.of(GensokyoLegacy.REG, Registries.LOOT_CONDITION_TYPE);

    public static final Val<LootItemConditionType> KILLED_BY_RUMIA =
            LIC.reg("killed_by_rumia", () -> new LootItemConditionType(KilledByRumiaHairbandCondition.CODEC));

    public static void register() {

    }

}
