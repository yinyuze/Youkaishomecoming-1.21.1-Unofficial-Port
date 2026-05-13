package dev.xkmc.youkaishomecoming.compat.terrablender;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.init.data.GLBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class GensokyoRegion extends Region {

    public GensokyoRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.FOREST, GLBiomes.SAKURA_FOREST);
            builder.replaceBiome(Biomes.FLOWER_FOREST, GLBiomes.SAKURA_FOREST);
            builder.replaceBiome(Biomes.BIRCH_FOREST, GLBiomes.SAKURA_BIRCH_FOREST);
            builder.replaceBiome(Biomes.OLD_GROWTH_BIRCH_FOREST, GLBiomes.SAKURA_BIRCH_FOREST);
            builder.replaceBiome(Biomes.TAIGA, GLBiomes.SAKURA_TAIGA);
            builder.replaceBiome(Biomes.OLD_GROWTH_SPRUCE_TAIGA, GLBiomes.SAKURA_TAIGA);
            builder.replaceBiome(Biomes.PLAINS, GLBiomes.ANIMAL_PATH);
        });
    }
}
