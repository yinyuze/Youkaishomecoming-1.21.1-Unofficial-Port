package dev.xkmc.youkaishomecoming.content.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.GLWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.Optional;

public class FlatStructure extends Structure {

    public static final MapCodec<FlatStructure> CODEC = RecordCodecBuilder.<FlatStructure>mapCodec((i) -> i.group(
            settingsCodec(i),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((e) -> e.startPool),
            Codec.intRange(0, 7).fieldOf("size").forGetter((e) -> e.maxDepth),
            Codec.BOOL.fieldOf("use_expansion_hack").forGetter((e) -> e.useExpansionHack),
            Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((e) -> e.maxDistanceFromCenter),
            Codec.intRange(1, 128).fieldOf("flat_check_range").forGetter((e) -> e.flatCheckRange),
            Codec.intRange(1, 128).fieldOf("height_tolerance").forGetter((e) -> e.flatTolerance)
    ).apply(i, FlatStructure::new)).validate(FlatStructure::verifyRange);

    public final Holder<StructureTemplatePool> startPool;
    private final int maxDepth;
    private final boolean useExpansionHack;
    private final int maxDistanceFromCenter;
    private final int flatCheckRange;
    private final int flatTolerance;

    private static DataResult<FlatStructure> verifyRange(FlatStructure s) {
        int extra = switch (s.terrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_THIN, BEARD_BOX, ENCAPSULATE -> 12;
        };
        return s.maxDistanceFromCenter + extra > 128
                ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128")
                : DataResult.success(s);
    }

    public FlatStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool,
                         int maxDepth, boolean useExpansionHack, int maxDistanceFromCenter,
                         int flatCheckRange, int flatTolerance) {
        super(settings);
        this.startPool = startPool;
        this.maxDepth = maxDepth;
        this.useExpansionHack = useExpansionHack;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.flatCheckRange = flatCheckRange;
        this.flatTolerance = flatTolerance;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext ctx) {
        ChunkPos chunkpos = ctx.chunkPos();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iz = -1; iz <= 1; iz++) {
                int x = chunkpos.getMiddleBlockX() + ix * flatCheckRange;
                int z = chunkpos.getMiddleBlockZ() + iz * flatCheckRange;
                int y = ctx.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState());
                if (y < min) min = y;
                if (y > max) max = y;
                if (y <= ctx.chunkGenerator().getSeaLevel()) return Optional.empty();
                var biome = ctx.chunkGenerator().getBiomeSource().getNoiseBiome(
                        QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), ctx.randomState().sampler());
                if (!biomes().contains(biome)) return Optional.empty();
            }
        }
        if (min + flatTolerance < max) return Optional.empty();
        int i = (max + min) / 2;
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        return JigsawPlacement.addPieces(ctx, this.startPool, Optional.empty(), this.maxDepth, blockpos,
                this.useExpansionHack, Optional.empty(), this.maxDistanceFromCenter,
                PoolAliasLookup.EMPTY, DimensionPadding.ZERO, LiquidSettings.APPLY_WATERLOGGING);
    }

    @Override
    public StructureType<?> type() {
        return GLWorldGen.FLAT.get();
    }

}
