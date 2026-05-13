package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class YHBiomeTagsProvider extends TagsProvider<Biome> {

	public static final TagKey<Biome> DEER = asTag("spawns/deer");
	public static final TagKey<Biome> BOAR = asTag("spawns/boar");
	public static final TagKey<Biome> CRAB = asTag("spawns/crab");
	public static final TagKey<Biome> CRAB_MUD = asTag("spawns/crab_mud");
	public static final TagKey<Biome> BLACK_GRAPE = asTag("spawns/black_grape");
	public static final TagKey<Biome> WHITE_GRAPE = asTag("spawns/white_grape");

	private static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(GensokyoLegacy.MODID, name));
	}

	public YHBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
		super(output, Registries.BIOME, provider, GensokyoLegacy.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
	}
}
