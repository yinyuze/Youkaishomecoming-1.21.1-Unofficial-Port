package dev.xkmc.youkaishomecoming.init.data.structure;

import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.youkaishomecoming.content.attachment.datamap.BedData;
import dev.xkmc.youkaishomecoming.content.attachment.datamap.StructureConfig;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.registrate.GLEntities;
import dev.xkmc.youkaishomecoming.init.registrate.GLMeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GLStructureGen {

    private static Block fdCabinet() {
        Block b = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("farmersdelight", "spruce_cabinet"));
        return b == null ? Blocks.AIR : b;
    }

    private static AppendLoot loot(String path) {
        return new AppendLoot(ResourceKey.create(Registries.LOOT_TABLE, GensokyoLegacy.loc(path)));
    }

    private static SetDataProcessor setData(Object... pairs) {
        var map = new HashMap<Block, net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier>();
        for (int i = 0; i < pairs.length; i += 2) {
            Block block = (Block) pairs[i];
            if (block != Blocks.AIR) map.put(block, (AppendLoot) pairs[i + 1]);
        }
        return new SetDataProcessor(Map.copyOf(map));
    }

    private static List<StructStructure> initStructures() {
        Block cabinet = fdCabinet();
        return List.<StructStructure>of(
                new StructStructure(
                        GensokyoLegacy.loc("cirno_nest"),
                        GLStructureTagGen.CIRNO_NEST,
                        32, 8,
                        StructureConfig.builder()
                                .room(2, 2, 1)
                                .primary(GLStructureTagGen.CIRNO_PRIMARY)
                                .wouldFix(GLStructureTagGen.CIRNO_FIX),
                        List.of(),
                        new StructSimpleBuilding(
                                List.of(
                                        new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE),
                                        setData(cabinet, loot("chests/cirno_nest/cabinet"))
                                ),
                                Map.of()
                        )
                ),
                new StructStructure(
                        GensokyoLegacy.loc("hakurei_shrine"),
                        GLStructureTagGen.HAKUREI_SHRINE,
                        24, 8,
                        StructureConfig.builder()
                                .room(2, 2, 1)
                                .primary(GLStructureTagGen.REIMU_PRIMARY)
                                .wouldFix(GLStructureTagGen.REIMU_FIX),
                        List.of(),
                        new FlatStructureBuilding(
                                List.of(
                                        new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE),
                                        setData(
                                                Blocks.CHEST, loot("chests/hakurei_shrine/chest"),
                                                Blocks.BARREL, loot("chests/hakurei_shrine/barrel"),
                                                cabinet, loot("chests/hakurei_shrine/cabinet")
                                        )
                                ),
                                Map.of(),
                                30, 6
                        )
                ),
                new StructStructure(
                        GensokyoLegacy.loc("youkai_nest"),
                        GLStructureTagGen.YOUKAI_NEST,
                        32, 8,
                        StructureConfig.builder()
                                .room(2, 2, 1),
                        List.of(),
                        new StructSimpleBuilding(
                                List.of(
                                        new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE),
                                        setData(
                                                Blocks.CHEST, loot("chests/youkai_nest/chest"),
                                                Blocks.BARREL, loot("chests/youkai_nest/barrel")
                                        )
                                ),
                                Map.of()
                        )
                )
        );
    }

    private static final Supplier<List<StructStructure>> STRUCTURES = Lazy.of(GLStructureGen::initStructures);

    public static void dataMap(RegistrateDataMapProvider pvd) {
        var bedReg = pvd.builder(GLMeta.BED_DATA.reg());
        var entityReg = pvd.builder(GLMeta.ENTITY_DATA.reg());
        var structureReg = pvd.builder(GLMeta.STRUCTURE_DATA.reg());
        for (var e : STRUCTURES.get()) {
            var config = e.config();
            for (var bedData : e.beds()) {
                for (var bed : bedData.bed())
                    bedReg.add(bed, new BedData(bedData.entity().value()), false);
                entityReg.add(bedData.entity(), bedData.data().withId(e.id()), false);
                config.addEntity(bedData.entity().value());
            }
            structureReg.add(e.id(), config.build(), false);
        }
    }

    public static void init(DataProviderInitializer init) {
        init.add(Registries.PROCESSOR_LIST, ctx -> {
            for (var e : STRUCTURES.get()) {
                e.building().registerProcessors(ctx, e.id());
            }
        });
        init.add(Registries.TEMPLATE_POOL, ctx -> {
            for (var e : STRUCTURES.get()) {
                e.building().registerTemplatePools(ctx, e.id());
            }
        });
        init.add(Registries.STRUCTURE, ctx -> {
            for (var e : STRUCTURES.get()) {
                var biome = ctx.lookup(Registries.BIOME).getOrThrow(e.biomes());
                e.building().registerStructure(ctx, e.id(), biome);
            }
        });
        init.add(Registries.STRUCTURE_SET, ctx -> {
            for (var e : STRUCTURES.get()) {
                var str = ctx.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, e.id()));
                ctx.register(ResourceKey.create(Registries.STRUCTURE_SET, e.id()), new StructureSet(
                        str, new RandomSpreadStructurePlacement(e.spacing(), e.separation(), RandomSpreadType.LINEAR, e.id().hashCode() & 0x7fffffff)));
            }
        });
    }

}
