package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.youkaishomecoming.content.world.FlatStructure;
import dev.xkmc.youkaishomecoming.content.world.NoisePlacement;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.structure.SetDataProcessor;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class GLWorldGen {

    private static final SR<StructureProcessorType<?>> PROCESSORS = SR.of(GensokyoLegacy.REG, Registries.STRUCTURE_PROCESSOR);
    private static final SR<StructureType<?>> STRUCTURE_TYPES = SR.of(GensokyoLegacy.REG, Registries.STRUCTURE_TYPE);
    private static final SR<PlacementModifierType<?>> PLACEMENTS = SR.of(GensokyoLegacy.REG, Registries.PLACEMENT_MODIFIER_TYPE);

    public static final Val<StructureProcessorType<SetDataProcessor>> SET_DATA = PROCESSORS.reg("set_data", () -> () -> SetDataProcessor.CODEC);
    public static final Val<StructureType<FlatStructure>> FLAT = STRUCTURE_TYPES.reg("flat_check", () -> () -> FlatStructure.CODEC);
    public static final Val<PlacementModifierType<NoisePlacement>> NOISE = PLACEMENTS.reg("noise", () -> () -> NoisePlacement.CODEC);

    public static void register() {

    }

}
