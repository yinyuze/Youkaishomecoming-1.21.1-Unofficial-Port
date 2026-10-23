package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.compat.sereneseasons.SeasonCompat;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;

public class YHTagGen {

	public static final TagKey<Item> RAW_EEL = item("raw_eel");
	public static final TagKey<Item> COOKED_EEL = item("cooked_eel");
	public static final TagKey<Item> DANGO = item("dango");
	public static final TagKey<Item> RAW_FLESH = item("raw_flesh");
	public static final TagKey<Item> APPARENT_FLESH_FOOD = item("apparent_flesh_food");
	public static final TagKey<Item> FLESH_FOOD = item("flesh_food");

	public static final TagKey<Item> RAW_TUNA = item("raw_tuna");
	public static final TagKey<Item> COOKED_TUNA = item("cooked_tuna");
	public static final TagKey<Item> OTORO = item("otoro");
	public static final TagKey<Item> RAW_CRAB = item("raw_crab");
	public static final TagKey<Item> COOKED_CRAB = item("cooked_crab");
	public static final TagKey<Item> CRAB_ROE = item("crab_roe");
	public static final TagKey<Item> RAW_VENISON = item("raw_venison");
	public static final TagKey<Item> COOKED_VENISON = item("cooked_venison");
	public static final TagKey<Item> RAW_BOAR = item("raw_boar");
	public static final TagKey<Item> COOKED_BOAR = item("cooked_boar");
	public static final TagKey<Item> IMITATION_CRAB = item("imitation_crab");
	public static final TagKey<Item> TAMAGOYAKI = item("tamagoyaki");
	public static final TagKey<Item> TAMAGOYAKI_SLICE = item("tamagoyaki_slice");
	public static final TagKey<Item> NATTOU = item("nattou");
	public static final TagKey<Item> CUCUMBER = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "crops/cucumber"));
	public static final TagKey<Item> CUCUMBER_SLICE = item("cucumber_slice");
	public static final TagKey<Item> SALMON_ROE = item("salmon_roe");
	public static final TagKey<Item> BUTTER = item("butter");
	public static final TagKey<Item> PLACE_WITH_CONTAINER = item("place_with_container");
	public static final TagKey<Item> STEAM_BLOCKER = item("steam_blocker");
	public static final TagKey<Item> IRON_BOWL_FOOD = item("iron_bowl_food");
	public static final TagKey<Item> BAMBOO = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "bamboo"));
	public static final TagKey<Item> SOYBEAN = item("soybean");
	public static final TagKey<Item> CARROT = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "vegetables/carrot"));
	public static final TagKey<Item> BROWN_MUSHROOM = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "mushrooms/brown"));
	public static final TagKey<Item> SOY_SAUCE_BOTTLE = item("soy_sauce_bottle");
	public static final TagKey<Item> MAYONNAISE_BOTTLE = item("mayonnaise_bottle");
	public static final TagKey<Item> TEA_DRINK = item("tea");
	public static final TagKey<Item> SAKE = item("sake");
	public static final TagKey<Item> WINE = item("wine");
	public static final TagKey<Item> BOTTLED = item("bottled");
	public static final TagKey<Item> COOKED_RICE = item("cuisine/cooked_rice");
	public static final TagKey<Item> DRIED_KELP = item("cuisine/dried_kelp");
	public static final TagKey<Item> GRAPE = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "fruits/grape"));
	public static final TagKey<Item> GRAPE_SEED = item("grape_seed");
	public static final TagKey<Block> FARMLAND_REDBEAN = block("farmland_redbean");
	public static final TagKey<Block> FARMLAND_COFFEA = block("farmland_coffea");
	public static final TagKey<Block> FARMLAND_SOYBEAN = block("farmland_soybean");
	public static final TagKey<Block> FARMLAND_TEA = block("farmland_tea");

	public static final TagKey<Item> MATCHA = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "matcha"));
	public static final TagKey<Item> MILK = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "foods/milk"));

	public static final TagKey<Item> TEA_GREEN = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tea_leaves/green"));
	public static final TagKey<Item> TEA_BLACK = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tea_leaves/black"));
	public static final TagKey<Item> TEA_WHITE = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tea_leaves/white"));
	public static final TagKey<Item> TEA_OOLONG = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tea_leaves/oolong"));
	public static final TagKey<Item> TEA = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tea_leaves"));

	public static final TagKey<Block> VERTICAL_SLAB = block("vertical_slab");
	public static final TagKey<Block> SIKKUI = block("sikkui");
	public static final TagKey<Block> CRAB_DIGABLE = block("crab_digable");

	// Soft compat tags from other mods. Created via ResourceLocation strings only — no
	// hard dependency on Quark / FTB Ultimine. If those mods aren't loaded, the tags are
	// simply unused. They tell those mods "don't blanket-handle this CropBlock as a
	// generic crop", which would otherwise reset our two-tall coffea/tea to age=0 and
	// orphan the upper half.
	public static final TagKey<Block> QUARK_SIMPLE_HARVEST_BLACKLIST =
			TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("quark", "simple_harvest_blacklisted"));
	public static final TagKey<Block> FTBULTIMINE_SINGLE_CROP_BLACKLIST =
			TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("ftbultimine", "single_crop_harvesting_blacklist"));

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
	}

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(FARMLAND_REDBEAN).add(Blocks.CLAY, Blocks.MUD, Blocks.COARSE_DIRT);
		pvd.addTag(FARMLAND_COFFEA).add(Blocks.PODZOL, Blocks.MUD, Blocks.SOUL_SOIL);
		pvd.addTag(FARMLAND_SOYBEAN).add(Blocks.FARMLAND);
		pvd.addTag(FARMLAND_TEA).add(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL);
		pvd.addTag(CRAB_DIGABLE).add(Blocks.SAND, Blocks.GRAVEL);

		// Opt our two-tall crops out of foreign right-click-harvest handlers (Quark
		// SimpleHarvest, FTB Ultimine single-crop harvest). Their generic CropBlock path
		// resets one half to age=0 via setBlock, leaving the other half orphaned.
		pvd.addTag(QUARK_SIMPLE_HARVEST_BLACKLIST)
				.add(YHCrops.COFFEA.getPlant(), YHCrops.TEA.getPlant());
		pvd.addTag(FTBULTIMINE_SINGLE_CROP_BLACKLIST)
				.add(YHCrops.COFFEA.getPlant(), YHCrops.TEA.getPlant());
		// Grape branches/fruits also extend BushBlock and would otherwise be reset to age=0
		// by Ultimine's single-crop harvest, bypassing our pickup() that triggers GRAPE_HARVEST.
		for (YHCrops grape : new YHCrops[]{YHCrops.RED_GRAPE, YHCrops.BLACK_GRAPE, YHCrops.WHITE_GRAPE}) {
			if (grape.vineSet != null) {
				pvd.addTag(FTBULTIMINE_SINGLE_CROP_BLACKLIST)
						.add(grape.vineSet.side.get(), grape.vineSet.hanging.get(), grape.vineSet.center.get());
			}
		}

		if (ModList.get().isLoaded("sereneseasons")) {
			SeasonCompat.genBlock(pvd);
		}
	}

	@SuppressWarnings("unchecked")
	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(MATCHA).add(YHItems.MATCHA.get())
				.addOptional(ResourceLocation.fromNamespaceAndPath("delightful", "matcha"));

		// Milk tag - includes milk bucket and milk bottle from Farmer's Delight
		pvd.addTag(MILK)
				.addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "milk_bottle"))
				.add(Items.MILK_BUCKET.builtInRegistryHolder().key());

		pvd.addTag(TEA_GREEN).add(YHTea.GREEN.leaves.get());
		pvd.addTag(TEA_BLACK).add(YHTea.BLACK.leaves.get());
		pvd.addTag(TEA_WHITE).add(YHTea.WHITE.leaves.get());
		pvd.addTag(TEA_OOLONG).add(YHTea.OOLONG.leaves.get());
		pvd.addTag(TEA).add(YHCrops.TEA.getFruits())
				.addTags(TEA_GREEN, TEA_BLACK, TEA_WHITE, TEA_OOLONG);

		// Cuisine tags for recipe ingredients
		pvd.addTag(COOKED_RICE)
				.addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "cooked_rice"));
		pvd.addTag(DRIED_KELP)
				.add(Items.DRIED_KELP.builtInRegistryHolder().key());
		pvd.addTag(BAMBOO)
				.add(Items.BAMBOO.builtInRegistryHolder().key());

		// c:grains/rice tag - add rice from FarmersDelight
		pvd.addTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "grains/rice")))
				.addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "rice"));

		// Grape tag - add all grape types
		pvd.addTag(GRAPE).add(YHCrops.RED_GRAPE.getFruits(), YHCrops.BLACK_GRAPE.getFruits(), YHCrops.WHITE_GRAPE.getFruits());
		pvd.addTag(GRAPE_SEED).add(YHCrops.RED_GRAPE.getSeed(), YHCrops.BLACK_GRAPE.getSeed(), YHCrops.WHITE_GRAPE.getSeed());

		pvd.addTag(SOYBEAN).add(YHCrops.SOYBEAN.getSeed());

		pvd.addTag(SOY_SAUCE_BOTTLE).add(YHItems.SOY_SAUCE_BOTTLE.asItem());
		pvd.addTag(MAYONNAISE_BOTTLE).add(YHItems.MAYONNAISE.asItem());

		pvd.addTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "foods/eggs")))
				.add(Items.EGG.builtInRegistryHolder().key());

		pvd.addTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "vegetables")))
				.add(Items.CARROT.builtInRegistryHolder().key())
				.add(Items.POTATO.builtInRegistryHolder().key())
				.add(Items.BEETROOT.builtInRegistryHolder().key())
				.add(YHCrops.CUCUMBER.getFruits().builtInRegistryHolder().key());
		pvd.addTag(CUCUMBER).add(YHCrops.CUCUMBER.getFruits());

		if (ModList.get().isLoaded("sereneseasons")) {
			SeasonCompat.genItem(pvd);
		}

	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(GensokyoLegacy.loc(id));
	}

	public static TagKey<Block> block(String id) {
		return BlockTags.create(GensokyoLegacy.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, GensokyoLegacy.loc(id));
	}

}
