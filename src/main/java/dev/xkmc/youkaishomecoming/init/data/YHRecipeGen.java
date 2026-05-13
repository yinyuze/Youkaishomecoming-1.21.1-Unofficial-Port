package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
//import dev.xkmc.fruitsdelight.init.FruitsDelight;
//import dev.xkmc.fruitsdelight.init.food.FDFood;
//import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2core.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotOutput;
import dev.xkmc.youkaishomecoming.content.pot.basin.SimpleBasinBuilder;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationBuilder;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.OrderedRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.MixedRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.FixedRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.food.TableBambooBowls;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.food.*;
import net.minecraft.world.item.DyeColor;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class YHRecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		{
			foodCut(pvd, YHFood.RAW_LAMPREY, YHFood.ROASTED_LAMPREY, YHFood.RAW_LAMPREY_FILLET, YHFood.ROASTED_LAMPREY_FILLET);
			// Tuna cuts into 3 slices
			food(pvd, YHFood.RAW_TUNA, YHFood.SEARED_TUNA);
			food(pvd, YHFood.RAW_TUNA_SLICE, YHFood.SEARED_TUNA_SLICE);
			cutting(pvd, YHFood.RAW_TUNA.item, YHFood.RAW_TUNA_SLICE.item, 3);
			// Venison and Boarchop cut into 2 pieces
			foodCut(pvd, YHFood.RAW_VENISON, YHFood.GRILLED_VENISON, YHFood.RAW_VENISON_SLICE, YHFood.GRILLED_VENISON_SLICE);
			foodCut(pvd, YHFood.RAW_BOARCHOP, YHFood.GRILLED_BOARCHOP, YHFood.RAW_BOARCHOP_BITS, YHFood.GRILLED_BOARCHOP_BITS);
			food(pvd, YHFood.FLESH, YHFood.COOKED_FLESH);
			food(pvd, YHFood.FLESH_SLICE, YHFood.COOKED_FLESH_SLICE);
			cutting(pvd, YHFood.FLESH.item, YHFood.FLESH_SLICE.item, 3);

			food(pvd, YHFood.TOFU, YHFood.OILY_BEAN_CURD);
			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHCrops.CUCUMBER.getFruits()),
							Ingredient.of(CommonTags.TOOLS_KNIFE), YHFood.CUCUMBER_SLICE.item, 2, 1)
					.build(pvd, YHCrops.CUCUMBER.fruits.getId().withSuffix("_cutting"));
			pvd.stonecutting(DataIngredient.items(Items.CLAY_BALL), RecipeCategory.MISC, YHItems.CLAY_SAUCER);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.RACK);
			pvd.smelting(DataIngredient.items(YHItems.CLAY_SAUCER.get()), RecipeCategory.MISC, YHItems.SAUCER, 0.1f, 200);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHItems.CAN.get(), 8)::unlockedBy, Items.IRON_NUGGET)
					.pattern("III").pattern("I I").pattern("III")
					.define('I', Items.IRON_NUGGET)
					.save(pvd);
		}

		// furniture
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.MOKA)::unlockedBy, YHItems.COFFEE_POWDER.get())
					.pattern("ABA").pattern("IWI").pattern("ADA")
					.define('A', Items.IRON_NUGGET)
					.define('I', Items.IRON_INGOT)
					.define('B', YHItems.COFFEE_POWDER)
					.define('D', Items.DEEPSLATE)
					.define('W', Items.WATER_BUCKET)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.KETTLE)::unlockedBy, YHCrops.TEA.getFruits())
					.pattern("ABA").pattern("IWI").pattern("AIA")
					.define('A', Items.IRON_NUGGET)
					.define('I', Items.IRON_INGOT)
					.define('B', YHCrops.TEA.getFruits())
					.define('W', Items.WATER_BUCKET)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.MOKA_KIT)::unlockedBy, Items.IRON_INGOT)
					.pattern("ABA").pattern("I I").pattern("EDE")
					.define('A', Items.IRON_NUGGET)
					.define('I', Items.IRON_INGOT)
					.define('B', Items.BLACK_DYE)
					.define('D', Items.DEEPSLATE)
					.define('E', Items.TERRACOTTA)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.FERMENT)::unlockedBy, Items.BUCKET)
					.pattern("ABA").pattern("ACA").pattern("AAA")
					.define('A', Items.MUD_BRICKS)
					.define('B', ItemTags.WOODEN_TRAPDOORS)
					.define('C', Items.BUCKET)
					.save(pvd);

			// cooking tools - small iron pot (IRON_BOWL) (3 iron ingots + 1 bowl)
			// Pattern: I B I
			//          空 I 空
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.IRON_BOWL)::unlockedBy, Items.IRON_INGOT)
					.pattern("IBI").pattern(" I ")
					.define('I', Items.IRON_INGOT)
					.define('B', Items.BOWL)
					.save(pvd);

			// small iron pot can also be made from stonecutting
			pvd.stonecutting(DataIngredient.items(Items.IRON_INGOT), RecipeCategory.MISC, YHBlocks.IRON_BOWL);

			// short iron pot (IRON_POT) (4 iron ingots + 1 bowl)
			// Pattern: I B I
			//          I   I
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.IRON_POT)::unlockedBy, Items.IRON_INGOT)
					.pattern("IBI").pattern("I I")
					.define('I', Items.IRON_INGOT)
					.define('B', Items.BOWL)
					.save(pvd);

			// iron pot can also be made from stonecutting
			pvd.stonecutting(DataIngredient.items(Items.IRON_INGOT), RecipeCategory.MISC, YHBlocks.IRON_POT);

			// wood basin (2 iron nuggets + 1 wooden slab + 1 wooden stairs)
			// Pattern: C 空 C
			//          B A B
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.BASIN)::unlockedBy, Items.IRON_NUGGET)
					.pattern("C C").pattern("BAB")
					.define('C', Items.IRON_NUGGET)
					.define('A', ItemTags.WOODEN_SLABS)
					.define('B', ItemTags.WOODEN_STAIRS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.BIG_SPOON)::unlockedBy, Items.BOWL)
					.pattern("  I").pattern(" I ").pattern("B  ")
					.define('I', Items.STICK)
					.define('B', Items.BOWL)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHBlocks.STOCKPOT)::unlockedBy, Items.IRON_INGOT)
					.requires(Items.CAULDRON)
					.requires(YHBlocks.BIG_SPOON)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.MOON_LANTERN)::unlockedBy, YHCrops.UDUMBARA.getFruits())
					.pattern("RAR").pattern("GFG").pattern("RAR")
					.define('A', Items.MANGROVE_PLANKS)
					.define('G', Items.AMETHYST_SHARD)
					.define('R', Items.END_ROD)
					.define('F', YHCrops.UDUMBARA.getFruits())
					.save(pvd);

			// cuisine board - stonecutting from bamboo block
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.CUISINE_BOARD);

			// steamer pot - shaped crafting
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.STEAMER_POT)::unlockedBy, Items.IRON_INGOT)
					.pattern("B B").pattern("I I").pattern("CIC")
					.define('B', Items.BAMBOO)
					.define('C', Items.CLAY_BALL)
					.define('I', Items.IRON_INGOT)
					.save(pvd);

			// steamer rack - stonecutting from bamboo block
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.STEAMER_RACK);

			// steamer lid - stonecutting from planks
			pvd.stonecutting(DataIngredient.tag(ItemTags.PLANKS), RecipeCategory.MISC, YHBlocks.STEAMER_LID);

		}

		// plants
		{
			cutting(pvd, YHCrops.SOYBEAN.fruits, YHCrops.SOYBEAN.seed, 1);
			cutting(pvd, YHCrops.COFFEA.fruits, YHCrops.COFFEA.seed, 1);
			pvd.smelting(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.smelting(DataIngredient.items(YHItems.STRIPPED_MANDRAKE_ROOT.get()), RecipeCategory.FOOD, YHFood.COOKED_MANDRAKE_ROOT.item, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHItems.STRIPPED_MANDRAKE_ROOT.get()), RecipeCategory.FOOD, YHFood.COOKED_MANDRAKE_ROOT.item, 0.1f, 200);
			cutting(pvd, YHCrops.MANDRAKE.seed, YHItems.STRIPPED_MANDRAKE_ROOT, 1);
			drying(pvd, DataIngredient.items(YHCrops.MANDRAKE.getFruits()), YHItems.DRIED_MANDRAKE_FLOWER);
			pvd.storage(YHCrops.SOYBEAN::getSeed, RecipeCategory.MISC, YHItems.SOYBEAN_BAG);
			pvd.storage(YHCrops.REDBEAN::getSeed, RecipeCategory.MISC, YHItems.REDBEAN_BAG);
			pvd.storage(YHItems.COFFEE_BEAN, RecipeCategory.MISC, YHItems.COFFEE_BEAN_BAG);
			pvd.storage(YHCrops.TEA::getFruits, RecipeCategory.MISC, YHItems.TEA_BAG);
			pvd.storage(YHTea.BLACK.leaves, RecipeCategory.MISC, YHItems.BLACK_TEA_BAG);
			pvd.storage(YHTea.GREEN.leaves, RecipeCategory.MISC, YHItems.GREEN_TEA_BAG);
			pvd.storage(YHTea.OOLONG.leaves, RecipeCategory.MISC, YHItems.OOLONG_TEA_BAG);
			pvd.storage(YHTea.WHITE.leaves, RecipeCategory.MISC, YHItems.WHITE_TEA_BAG);

			// Drying rack recipes
			drying(pvd, DataIngredient.tag(YHTagGen.GRAPE), YHFood.RAISIN.item);
			drying(pvd, DataIngredient.items(Items.KELP), () -> Items.DRIED_KELP);
			drying(pvd, DataIngredient.tag(ItemTags.SAPLINGS), () -> Items.DEAD_BUSH);
			drying(pvd, DataIngredient.items(Items.ROTTEN_FLESH), () -> Items.LEATHER, 18000);
			drying(pvd, DataIngredient.items(YHCrops.TEA.getFruits()), YHTea.WHITE.leaves);
			// Note: YELLOW tea doesn't exist in GL version, skipping that recipe

			// Steaming recipes
			steaming(pvd, DataIngredient.items(YHCrops.TEA.getFruits()), YHTea.GREEN.leaves);
			steaming(pvd, DataIngredient.items(YHFood.CRAB.item.get()), YHFood.STEAMED_CRAB.item);
			steaming(pvd, DataIngredient.items(Items.POTATO), () -> Items.BAKED_POTATO);
			steaming(pvd, DataIngredient.items(ModItems.CHICKEN_CUTS.get()), ModItems.COOKED_CHICKEN_CUTS);
			steaming(pvd, DataIngredient.items(ModItems.SALMON_SLICE.get()), ModItems.COOKED_SALMON_SLICE);
			steaming(pvd, DataIngredient.items(ModItems.COD_SLICE.get()), ModItems.COOKED_COD_SLICE);
			steaming(pvd, DataIngredient.items(YHItems.RAW_BUN.get()), YHFood.BUN.item);
			steaming(pvd, DataIngredient.tag(TagRef.DOUGH_WHEAT), YHFood.MANTOU.item);
			steaming(pvd, DataIngredient.items(YHItems.RAW_OYAKI.get()), YHFood.OYAKI.item);

			pvd.smoking(DataIngredient.items(YHTea.GREEN.leaves.get()), RecipeCategory.MISC, YHTea.BLACK.leaves, 0.1f, 200);
			pvd.campfire(DataIngredient.items(YHTea.GREEN.leaves.get()), RecipeCategory.MISC, YHTea.OOLONG.leaves, 0.1f, 200);

			cutSave(CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON_BUCKET),
							Ingredient.of(CommonTags.TOOLS_KNIFE), Items.WATER_BUCKET, 1)
					.addResult(ModItems.SALMON_SLICE.get(), 2)
					.addResult(Items.BONE_MEAL)
					.addResultWithChance(YHFood.ROE.item.get(), 0.5f, 1), pvd, "salmon_bucket");

			cutSave(CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHItems.COFFEE_BEAN),
							Ingredient.of(ItemTags.SHOVELS), YHItems.COFFEE_POWDER, 1), pvd, "coffee_beans");

			cutSave(CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHTea.GREEN.leaves),
							Ingredient.of(ItemTags.SHOVELS), YHItems.MATCHA, 1), pvd, "green_tea_leaves");

			cutSave(CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ICE),
							Ingredient.of(ItemTags.PICKAXES), YHItems.ICE_CUBE, 8), pvd, "ice");

			//drying(pvd, DataIngredient.items(YHTea.GREEN.leaves.get()), YHTea.WHITE.leaves);
		}

		// food craft
		{
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.MILK_POPSICLE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.pattern(" MM").pattern("SIM").pattern("TS ")
					.define('M', CommonTags.FOODS_MILK)
					.define('S', Items.SUGAR)
					.define('I', YHItems.ICE_CUBE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.BIG_POPSICLE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.pattern(" II").pattern("SII").pattern("TS ")
					.define('S', Items.SUGAR)
					.define('I', YHItems.ICE_CUBE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.ASSORTED_DANGO.item, 1)::unlockedBy, YHFood.MOCHI.item.get())
					.requires(YHFood.MOCHI.item).requires(YHFood.MATCHA_MOCHI.item).requires(YHFood.SAKURA_MOCHI.item).requires(Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.KINAKO_DANGO.item, 1)::unlockedBy, YHFood.MOCHI.item.get())
					.requires(YHTagGen.DANGO).requires(YHTagGen.DANGO).requires(YHTagGen.DANGO)
					.requires(YHCrops.SOYBEAN.getSeed()).requires(Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.SHAVED_ICE_OVER_RICE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.requires(CommonTags.CROPS_RICE).requires(YHItems.ICE_CUBE).requires(YHCrops.REDBEAN.getSeed())
					.requires(ModItems.COD_ROLL.get())
					.save(pvd);


			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi.TOBIKO_GUNKAN.item, 2)::unlockedBy, YHFood.ROE.item.get())
					.requires(YHFood.ROE.item).requires(ModItems.COOKED_RICE.get()).requires(Items.DRIED_KELP)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHItems.RAW_BUN, 4)::unlockedBy, Items.WHEAT)
					.requires(TagRef.DOUGH)
					.requires(TagRef.DOUGH)
					.requires(TagRef.FOOD_CABBAGE)
					.requires(TagRef.VEGETABLES_ONION)
					.requires(YHTagGen.SOYBEAN)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHItems.RAW_OYAKI, 4)::unlockedBy, Items.WHEAT)
					.requires(TagRef.DOUGH)
					.requires(TagRef.DOUGH)
					.requires(TagRef.VEGETABLES)
					.requires(Items.BROWN_MUSHROOM)
					.save(pvd);

			cake(pvd, YHItems.TARTE_LUNE);

			cake(pvd, YHItems.RED_VELVET);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.RED_VELVET.block, 1)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("FBF").pattern("ADA").pattern("ECE")
					.define('A', Items.SUGAR)
					.define('B', Items.MILK_BUCKET)
					.define('C', Items.GLASS_BOTTLE)
					.define('D', YHTagGen.RAW_FLESH)
					.define('E', Items.WHEAT)
					.define('F', Items.COCOA_BEANS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.TARTE_LUNE.block, 1)::unlockedBy, ModItems.PIE_CRUST.get())
					.pattern("FBF").pattern("DCD").pattern("AEA")
					.define('A', Items.SUGAR)
					.define('B', Items.ALLIUM)
					.define('D', Items.CORNFLOWER)
					.define('F', Items.WHEAT)
					.define('C', YHItems.CREAM)
					.define('E', ModItems.PIE_CRUST.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.DOUGHNUT.item, 4)::unlockedBy, ModItems.WHEAT_DOUGH.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', CommonTags.FOODS_DOUGH)
					.define('B', YHItems.CREAM)
					.define('C', Items.SUGAR)
					.save(pvd);

		}

		// food cooking
		{

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUTTER.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(CommonTags.FOODS_MILK), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHItems.MAYONNAISE.item.get(), 2, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(TagRef.EGGS)
					.addIngredient(Items.SUGAR), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.IMITATION_CRAB.item.get(), 4, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(Items.WHEAT)
					.addIngredient(Items.WHEAT)
					.addIngredient(Items.SUGAR), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TAMAGOYAKI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(Items.SUGAR), pvd);

			cutting(pvd, YHFood.TAMAGOYAKI.item, YHFood.TAMAGOYAKI_SLICE.item, 3);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.KABAYAKI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LAMPREY_SKEWER.item.get(), 1, 200, 0.1f, Items.STICK)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TOFU.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed()), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.ONIGILI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.KELP), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEKIBANKIYAKI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHFood.BUTTER.item), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed()), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TSUKIMI_DANGO.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.SOYBEAN.getSeed()), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YASHOUMA_DANGO.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.PINK_DYE)
					.addIngredient(Items.GREEN_DYE), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAKURA_MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.COFFEE_MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHItems.COFFEE_POWDER), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MATCHA_MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHTagGen.MATCHA), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SENBEI.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.KELP), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YAKUMO_INARI.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.CARROT)
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get()), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUN.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHCrops.SOYBEAN.getSeed()), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.OYAKI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.BROWN_MUSHROOM), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.PORK_RICE_BALL.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(CommonTags.FOODS_RAW_PORK), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.TUTU_CONGEE.item.get().asItem(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.BAMBOO), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.STEAMED_EGG_IN_BAMBOO.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.BAMBOO), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANDY_APPLE.item.get(), 1, 200, 0.1f, Items.STICK)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.APPLE)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR), pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.FAIRY_CANDY.item, 4)::unlockedBy, GLItems.FAIRY_ICE_CRYSTAL.get())
					.requires(GLItems.FAIRY_ICE_CRYSTAL)
					.requires(Items.SUGAR)
					.requires(Items.SUGAR)
					.requires(Items.SUGAR)
					.save(pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.KOISHI_MOUSSE.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.ALLIUM)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM), pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_CHOCOLATE.item, 3)::unlockedBy, Items.COCOA_BEANS)
					.requires(YHItems.MATCHA).requires(Items.TWISTING_VINES).requires(Items.PINK_PETALS)
					.requires(Items.HONEY_BOTTLE).requires(Items.BLAZE_POWDER).requires(Items.BLUE_ORCHID)
					.requires(Items.COCOA_BEANS, 3)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_DOUGHNUT.item, 1)::unlockedBy, YHFood.HIGI_CHOCOLATE.item.get())
					.requires(YHFood.DOUGHNUT.item).requires(YHFood.HIGI_CHOCOLATE.item)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.FLESH_CHOCOLATE_MOUSSE.item, 4)::unlockedBy, YHFood.FLESH.item.get())
					.pattern(" B ").pattern("FDF").pattern("ECE")
					.define('B', Items.MILK_BUCKET)
					.define('C', Items.GLASS_BOTTLE)
					.define('D', YHTagGen.RAW_FLESH)
					.define('E', Items.WHEAT)
					.define('F', Items.COCOA_BEANS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.SCARLET_DEVIL_CAKE.item, 4)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("FBF").pattern("ADA").pattern("ECE")
					.define('A', Items.HONEY_BOTTLE)
					.define('B', Items.MILK_BUCKET)
					.define('C', Items.GLASS_BOTTLE)
					.define('D', YHTagGen.RAW_FLESH)
					.define('E', Items.WHEAT)
					.define('F', Items.COCOA_BEANS)
					.save(pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_DUMPLINGS.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(Items.CARROT), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANNED_FLESH.item.get(), 1, 200, 0.1f, YHItems.CAN.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_STEW.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Items.CARROT)
					.addIngredient(Items.CARROT)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BOWL_OF_FLESH_FEAST.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MITARASHI_DANGO.item.get(), 1, 200, 0.1f, Items.STICK)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHItems.SURP_CHEST.get(), 1, 200, 0.1f, Items.CHEST)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.addIngredient(Items.PURPLE_BANNER), pvd);


		}

		// food cooking bowl
		{
			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.APAKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.PINK_PETALS), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.AVGOLEMONO.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.GLOW_BERRIES)
					.addIngredient(Items.GLOW_BERRIES), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BLAZING_RED_CURRY.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.BLAZE_POWDER)
					.addIngredient(Tags.Items.CROPS_POTATO)
					.addIngredient(CommonTags.FOODS_RAW_CHICKEN), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GRILLED_EEL_OVER_RICE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.HIGAN_SOUP.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Tags.Items.FOODS_VEGETABLE), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LONGEVITY_NOODLES.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_PASTA)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_RAW_PORK), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.SEAFOOD_MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.addIngredient(CommonTags.FOODS_RAW_SALMON), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.POOR_GOD_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Tags.Items.SEEDS)
					.addIngredient(Tags.Items.CROPS)
					.addIngredient(ItemTags.FLOWERS)
					.addIngredient(Items.BONE_MEAL), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.POWER_SOUP.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.KELP)
					.addIngredient(Items.KELP)
					.addIngredient(CommonTags.FOODS_ONION), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SHIRAYUKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Items.KELP)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(Tags.Items.FOODS_VEGETABLE), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(Items.CARROT)
					.addIngredient(Tags.Items.FOODS_VEGETABLE), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHItems.CREAM.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(CommonTags.FOODS_MILK), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUSCAN_SALMON.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.addIngredient(CommonTags.FOODS_TOMATO)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(YHItems.CREAM.get()), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.MUSHROOM_SOUP.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.CREAM.get()), pvd);

			// Additional iron bowl foods (temporarily disabled - missing textures)
			/*
			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.POTATO_SOUP.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHItems.CREAM.get())
					.addIngredient(Items.POTATO)
					.addIngredient(Items.POTATO)
					.addIngredient(Items.POTATO)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_ONION), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.BORSCHT.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHItems.CREAM.get())
					.addIngredient(Items.BEETROOT)
					.addIngredient(Items.BEETROOT)
					.addIngredient(Items.BEETROOT)
					.addIngredient(CommonTags.FOODS_TOMATO)
					.addIngredient(Items.POTATO)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(CommonTags.FOODS_RAW_BEEF), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.SIGNATURE_MUSHROOM_STEW.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Tags.Items.MUSHROOMS), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHBowl.HOKKAIDO_SALMON_HOTPOT.item.get(), 1, 200, 0.1f, YHBlocks.IRON_BOWL.asItem())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(Items.KELP)
					.addIngredient(Tags.Items.CROPS_CARROT)
					.addIngredient(Items.POTATO)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(CommonTags.FOODS_RAW_SALMON), pvd);
			*/

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LIONS_HEAD.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Tags.Items.CROPS_CARROT)
					.addIngredient(CommonTags.FOODS_CABBAGE), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MAPO_TOFU.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.BLAZE_POWDER), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHFood.UDUMBARA_CAKE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(YHCrops.UDUMBARA.getFruits()), pvd);
		}

		// food cooking saucer
		{
			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BAMBOO_MIZUYOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(Items.BAMBOO)
					.addIngredient(Items.SUGAR), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.DRIED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.IMITATION_BEAR_PAW.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(Items.BAMBOO)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.PASTITSIO.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_PASTA)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(CommonTags.FOODS_RAW_BEEF)
					.addIngredient(CommonTags.FOODS_ONION), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SAUCE_GRILLED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(CommonTags.FOODS_ONION), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.STINKY_TOFU.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(Items.BROWN_MUSHROOM), pvd);

			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOFU_BURGER.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.SWEET_BERRIES), pvd);


			cookSave(CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SEVEN_COLORED_YOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHORUS_FRUIT)
					.addIngredient(YHTagGen.MATCHA)
					.addIngredient(YHCrops.UDUMBARA.getFruits()), pvd);

		}

		var tea = tea(pvd);
		var coffee = coffee(pvd);

		// drinks
		{

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHTagGen.TEA_BLACK)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GREEN_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.OOLONG_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.OOLONG.leaves)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.WHITE_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.WHITE.leaves)
					.addIngredient(YHTagGen.TEA_WHITE)
					.addIngredient(YHTagGen.TEA_WHITE)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.CORNFLOWER_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.TEA_MOCHA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.COCOA_BEANS)
					.addIngredient(CommonTags.FOODS_MILK)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.SAIDI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.SAKURA_HONEY_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GENMAI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(CommonTags.CROPS_RICE)
					.build(tea);


			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GREEN_WATER.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.GLASS_BOTTLE)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHCoffee.ESPRESSO.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.COFFEE_POWDER)
					.build(coffee);

			coffee(coffee, YHCoffee.RISTRETTO, 1, e -> e.addIngredient(YHItems.COFFEE_POWDER));
			coffee(coffee, YHCoffee.AMERICANO, 2, e -> e);
			coffee(coffee, YHCoffee.LATTE, 2, e -> e.addIngredient(CommonTags.FOODS_MILK));
			coffee(coffee, YHCoffee.MOCHA, 2, e -> e
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(Items.COCOA_BEANS));
			coffee(coffee, YHCoffee.CAPPUCCINO, 2, e -> e
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.MACCHIATO, 2, e -> e.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.CON_PANNA, 1, e -> e
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.AFFOGATO, 2, e -> e
					.addIngredient(YHItems.ICE_CUBE)
					.addIngredient(YHItems.CREAM));

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHCoffee.AFFOGATO.item.get(), 1)::unlockedBy,
					YHCoffee.ESPRESSO.item.get()).requires(YHCoffee.ESPRESSO.item)
					.requires(YHItems.ICE_CUBE).requires(YHItems.CREAM)
					.save(coffee, YHCoffee.AFFOGATO.item.getId().withSuffix("_craft"));

		}

		// cooking pots (small iron pot, short iron pot, stockpot)
		{
			var soy = YHItems.SOY_SAUCE_BOTTLE.item.get();

			// Small iron pot (IRON_BOWL) recipes - 200 ticks
			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHBowl.HIGAN_SOUP, 200)::unlockedBy, YHBlocks.IRON_BOWL.asItem())
					.add(Items.SOUL_SAND)
					.add(Tags.Items.CROPS)
					.save(pvd);

			// Short iron pot (IRON_POT) recipes - 200 ticks
			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.SHIRAYUKI, 200)::unlockedBy, YHBlocks.IRON_POT.asItem())
					.add(YHFood.TOFU.item.get())
					.add(Items.KELP)
					.add(Tags.Items.FOODS_VEGETABLE)
					.add(Items.PUFFERFISH)
					.add(YHTagGen.RAW_EEL)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.COD_STEW, 200)::unlockedBy, YHBlocks.IRON_POT.asItem())
					.add(CommonTags.FOODS_SAFE_RAW_FISH)
					.add(Tags.Items.EGGS)
					.add(CommonTags.FOODS_TOMATO)
					.add(Items.POTATO)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.HAN_PALACE, 200)::unlockedBy, YHBlocks.IRON_POT.asItem())
					.add(YHFood.TOFU.item.get())
					.add(YHFood.TOFU.item.get())
					.add(YHFood.RAW_LAMPREY.item.get())
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.TOFU_CRAB_STEW, 200)::unlockedBy, YHBlocks.IRON_POT.asItem())
					.add(YHFood.TOFU.item.get())
					.add(YHFood.CRAB_MEAT.item.get())
					.add(YHFood.CRAB.item.get())
					.save(pvd);

			// Stockpot (STOCKPOT) recipes - 400 ticks
			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.MISO_SOUP, 400)::unlockedBy, YHBlocks.STOCKPOT.asItem())
					.add(soy).add(YHCrops.SOYBEAN.getSeed())
					.add(YHFood.TOFU.item.get())
					.add(YHFood.TOFU.item.get())
					.add(YHFood.TOFU.item.get())
					.add(Items.DRIED_KELP)
					.add(Items.DRIED_KELP)
					.add(Items.BROWN_MUSHROOM)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.SEAFOOD_MISO_SOUP, 400)::unlockedBy, YHBlocks.STOCKPOT.asItem())
					.add(soy).add(YHCrops.SOYBEAN.getSeed())
					.add(YHFood.TOFU.item.get())
					.add(YHFood.TOFU.item.get())
					.add(Items.DRIED_KELP)
					.add(Items.DRIED_KELP)
					.add(Items.BROWN_MUSHROOM)
					.add(CommonTags.FOODS_ONION)
					.add(CommonTags.FOODS_SAFE_RAW_FISH)
					.add(CommonTags.FOODS_SAFE_RAW_FISH)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.POWER_SOUP, 400)::unlockedBy, YHBlocks.STOCKPOT.asItem())
					.add(soy).add(YHCrops.SOYBEAN.getSeed())
					.add(YHTagGen.RAW_BOAR)
					.add(YHTagGen.RAW_VENISON)
					.add(Items.KELP)
					.add(Items.CARROT)
					.add(CommonTags.FOODS_TOMATO)
					.add(CommonTags.FOODS_CABBAGE)
					.add(CommonTags.FOODS_ONION)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.MUSHROOM_SOUP, 400)::unlockedBy, YHBlocks.STOCKPOT.asItem())
					.add(YHItems.CREAM.get())
					.add(Items.BROWN_MUSHROOM)
					.add(Items.BROWN_MUSHROOM)
					.add(Items.BROWN_MUSHROOM)
					.add(CommonTags.FOODS_ONION)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.POTATO_SOUP, 400)::unlockedBy, YHBlocks.STOCKPOT.asItem())
					.add(YHItems.CREAM.get())
					.add(Items.POTATO)
					.add(Items.POTATO)
					.add(Items.POTATO)
					.add(CommonTags.FOODS_RAW_PORK)
					.add(CommonTags.FOODS_ONION)
					.save(pvd);

			unlock(pvd, new dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedPotRecipeBuilder(YHPotFood.BORSCHT, 400)::unlockedBy, YHBlocks.STOCKPOT.asItem())
					.add(YHItems.CREAM.get())
					.add(Items.BEETROOT)
					.add(Items.BEETROOT)
					.add(Items.BEETROOT)
					.add(CommonTags.FOODS_TOMATO)
					.add(CommonTags.FOODS_TOMATO)
					.add(Items.POTATO)
					.add(CommonTags.FOODS_ONION)
					.add(CommonTags.FOODS_RAW_BEEF)
					.save(pvd);
		}

		// wine
		{

			unlock(pvd, new SimpleBasinBuilder(YHDrink.BLACK_GRAPE_JUICE.fluid.getSource(), 50)::unlockedBy,
					YHCrops.BLACK_GRAPE.getFruits())
					.setInput(YHCrops.BLACK_GRAPE.getFruits())
					.save(pvd, YHDrink.BLACK_GRAPE_JUICE.item.getId());

			unlock(pvd, new SimpleBasinBuilder(YHDrink.RED_GRAPE_JUICE.fluid.getSource(), 50)::unlockedBy,
					YHCrops.RED_GRAPE.getFruits())
					.setInput(YHCrops.RED_GRAPE.getFruits())
					.save(pvd, YHDrink.RED_GRAPE_JUICE.item.getId());

			unlock(pvd, new SimpleBasinBuilder(YHDrink.WHITE_GRAPE_JUICE.fluid.getSource(), 50)::unlockedBy,
					YHCrops.WHITE_GRAPE.getFruits())
					.setInput(YHCrops.WHITE_GRAPE.getFruits())
					.save(pvd, YHDrink.WHITE_GRAPE_JUICE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.WHITE_GRAPE_JUICE,
							YHDrink.WHITE_WINE, 1800)::unlockedBy,
					YHCrops.WHITE_GRAPE.getFruits())
					.save(pvd, YHDrink.WHITE_WINE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.RED_GRAPE_JUICE,
							YHDrink.RED_WINE, 1800)::unlockedBy,
					YHCrops.RED_GRAPE.getFruits())
					.save(pvd, YHDrink.RED_WINE.item.getId());


			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.RED_GRAPE_JUICE,
							YHDrink.VAN_ALLEN, 2400)::unlockedBy,
					YHCrops.RED_GRAPE.getFruits())
					.addInput(Items.SWEET_BERRIES)
					.save(pvd, YHDrink.VAN_ALLEN.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.BLACK_GRAPE_JUICE,
							YHDrink.BURGUNDY, 2400)::unlockedBy,
					YHCrops.BLACK_GRAPE.getFruits())
					.addInput(Items.SWEET_BERRIES)
					.save(pvd, YHDrink.BURGUNDY.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.WHITE_GRAPE_JUICE,
							YHDrink.CHAMPAGNE, 2400)::unlockedBy,
					YHCrops.WHITE_GRAPE.getFruits())
					.addInput(Items.SWEET_BERRIES)
					.addInput(Items.SUGAR)
					.save(pvd, YHDrink.CHAMPAGNE.item.getId());
		}

		// sake
		{

			// SOY_SAUCE_BOTTLE is not IYHSake, commenting out
			// unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHItems.SOY_SAUCE_BOTTLE, 1800)::unlockedBy, YHCrops.SOYBEAN.getSeed())
			// 		.addInput(YHCrops.SOYBEAN.getSeed()).addInput(YHCrops.SOYBEAN.getSeed())
			// 		.addInput(YHCrops.SOYBEAN.getSeed()).addInput(YHCrops.SOYBEAN.getSeed())
			// 		.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder("minecraft:water",
							new net.neoforged.neoforge.fluids.FluidStack(YHItems.SOY_SAUCE_BOTTLE.source(), 1000), 1800)::unlockedBy,
					YHCrops.SOYBEAN.getSeed())
					.addInput(YHTagGen.SOYBEAN)
					.addInput(YHTagGen.SOYBEAN)
					.addInput(YHTagGen.SOYBEAN)
					.addInput(YHTagGen.SOYBEAN)
					.save(pvd, YHItems.SOY_SAUCE_BOTTLE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.MIO, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.MEAD, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.HONEY_BOTTLE)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.KIKU, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.BROWN_MUSHROOM)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.HAKUTSURU, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.BROWN_MUSHROOM).addInput(TagRef.EGGS)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.KAPPA_VILLAGE, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.BROWN_MUSHROOM).addInput(Items.SEAGRASS)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.SUIGEI, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.SEA_PICKLE).addInput(Items.KELP).addInput(Items.PUFFERFISH)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.DAIGINJO, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.BLAZE_POWDER)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.DASSAI, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.NAUTILUS_SHELL)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.TENGU_TANGO, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.PHANTOM_MEMBRANE)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.SPARROW_SAKE, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.FEATHER).addInput(Items.RABBIT_FOOT)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.FULL_MOONS_EVE, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(YHCrops.UDUMBARA.getFruits())
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(YHItems.BLOOD_BOTTLE, YHDrink.SCARLET_MIST, 3600)::unlockedBy, ModItems.RICE.get())
					.addInput(Items.ROSE_BUSH).addInput(Items.ROSE_BUSH)
					.addInput(Items.POPPY)
					.addInput(DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get())
					.addInput(DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get())
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.WIND_PRIESTESSES, 3600)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(DanmakuItems.Bullet.CIRCLE.get(DyeColor.LIME).get())
					.addInput(Items.DANDELION).addInput(YHTagGen.TEA_GREEN).addInput(YHItems.MATCHA.get())
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.YELLOW_TEA, 1800)::unlockedBy, YHTea.GREEN.leaves.get())
					.addInput(YHTagGen.TEA_GREEN).addInput(YHTagGen.TEA_GREEN)
					.addInput(Items.DANDELION)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.DARK_TEA, 2400)::unlockedBy, YHTea.BLACK.leaves.get())
					.addInput(YHTagGen.TEA_BLACK).addInput(YHTagGen.TEA_BLACK)
					.addInput(Items.BROWN_MUSHROOM)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.SCARLET_TEA, 3600)::unlockedBy, YHTea.BLACK.leaves.get())
					.addInput(YHTagGen.TEA_BLACK).addInput(YHTagGen.TEA_BLACK)
					.addInput(Items.ROSE_BUSH).addInput(Items.POPPY)
					.addInput(YHTagGen.FLESH_FOOD)
					.save(pvd);

		}

		//if (ModList.get().isLoaded(FruitsDelight.MODID)) {
		//	RecipeOutput modtea = new ConditionalRecipeWrapper(tea, new ModLoadedCondition(FruitsDelight.MODID));
		//	CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.MOON_ROCKET.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
		//			.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
		//			.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
		//			.addIngredient(FDFood.LEMON_SLICE.get())
		//			.addIngredient(Items.SUGAR)
		//			.build(modtea);
		//
		//	CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.LEMON_BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
		//			.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
		//			.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
		//			.addIngredient(FDFood.LEMON_SLICE.get())
		//			.addIngredient(YHTagGen.TEA_BLACK)
		//			.addIngredient(Items.SUGAR)
		//			.build(modtea);
		//
		//	CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.PEACH_TAPIOCA.item.get(), 1, 200, 0.1f, Items.BOWL)
		//			.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
		//			.addIngredient(FruitType.PEACH.getFruitTag())
		//			.addIngredient(Items.LILY_PAD)
		//			.build(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID));
		//
		//
		//	unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FruitsDelightCompatFood.PEACH_YATSUHASHI.item, 2)::unlockedBy, FruitType.PEACH.getFruit())
		//			.requires(FruitType.PEACH.getFruitTag())
		//			.requires(ModItems.COOKED_RICE.get())
		//			.save(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID));
		//
		//}

		// cuisine
		{

			for (var e : YHRolls.values()) {
				var slice = e.sliceStack();
				if (slice.isEmpty()) continue;
				cutting(pvd, e.item, e.slice, slice.getCount());
			}


			{

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(ModItems.SALMON_ROLL.get(), 2)
						.add(CommonTags.FOODS_RAW_SALMON)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, ModItems.COD_SLICE.get())
						.result(ModItems.COD_ROLL.get(), 2)
						.add(CommonTags.FOODS_SAFE_RAW_FISH)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHSushi.TUNA_NIGIRI.item.get(), 2)
						.add(YHTagGen.RAW_TUNA)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.OTORO.item.get())
						.result(YHSushi.OTORO_NIGIRI.item.get(), 2)
						.add(YHFood.OTORO.item)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.ROASTED_LAMPREY_FILLET.item.get())
						.result(YHSushi.LORELEI_NIGIRI.item.get(), 2)
						.add(YHFood.KABAYAKI.item.get())
						.add(Items.DRIED_KELP)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.TAMAGOYAKI.item.get())
						.result(YHSushi.EGG_NIGIRI.item.get(), 2)
						.add(YHFood.TAMAGOYAKI.item.get())
						.add(Items.DRIED_KELP)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.FLESH.item.get())
						.result(YHSushi.FLESH_ROLL.item.get(), 2)
						.add(YHTagGen.RAW_FLESH)
						.add(Items.DRIED_KELP)
						.save(pvd);

			}

			{

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_GUNKAN)::unlockedBy, YHFood.ROE.item.get())
						.result(YHSushi.TOBIKO_GUNKAN, 2)
						.add(YHFood.ROE.item.get())
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_GUNKAN)::unlockedBy, YHFood.NATTOU.item.get())
						.result(YHSushi.NATTOU_GUNKAN, 2)
						.add(YHFood.NATTOU.item.get())
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_GUNKAN)::unlockedBy, Items.SEAGRASS)
						.result(YHSushi.SEAGRASS_GUNKAN, 2)
						.add(Items.SEAGRASS)
						.save(pvd);
			}

			{
				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, ModItems.COOKED_RICE.get())
						.result(ModItems.KELP_ROLL.get())
						.add(Items.CARROT)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHRolls.TEKKA_MAKI)
						.add(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.add(YHTagGen.RAW_TUNA)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, ModItems.COOKED_RICE.get())
						.result(YHRolls.SHINNKO_MAKI)
						.add(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.add(Items.BEETROOT)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, ModItems.COOKED_RICE.get())
						.result(YHRolls.KAPPA_MAKI)
						.add(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.add(YHTagGen.CUCUMBER_SLICE)
						.save(pvd);
			}

			{
				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_FUTOMAKI)::unlockedBy, YHFood.TAMAGOYAKI_SLICE.item.get())
						.result(YHRolls.EGG_FUTOMAKI)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_FUTOMAKI)::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(YHRolls.SALMON_FUTOMAKI)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHTagGen.CUCUMBER_SLICE)
						.addUnordered(Ingredient.of(Items.CARROT, Items.BEETROOT))
						.addUnordered(YHFood.IMITATION_CRAB.item.asItem())
						.addUnordered(CommonTags.FOODS_RAW_SALMON)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_FUTOMAKI)::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(YHRolls.RAINBOW_FUTOMAKI)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHTagGen.CUCUMBER_SLICE)
						.addUnordered(Ingredient.of(Items.CARROT, Items.BEETROOT))
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.IMITATION_CRAB.item.asItem())
						.addUnordered(CommonTags.FOODS_RAW_SALMON)
						.save(pvd);
			}

			{
				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_CAL)::unlockedBy, YHFood.IMITATION_CRAB.item.get())
						.result(YHRolls.CALIFORNIA_ROLL)
						.addOrdered(YHItems.MAYONNAISE.item.get())
						.addUnordered(YHTagGen.CUCUMBER_SLICE)
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.IMITATION_CRAB.item.get())
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, YHFood.CRAB_ROE.item.get())
						.result(YHRolls.ROE_CALIFORNIA_ROLL)
						.add(YHFood.CRAB_ROE.item.get())
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(YHRolls.SALMON_LOVER_ROLL)
						.addOrdered(YHFood.CRAB_ROE.item.get())
						.addUnordered(CommonTags.FOODS_RAW_SALMON)
						.addUnordered(CommonTags.FOODS_RAW_SALMON)
						.addUnordered(CommonTags.FOODS_RAW_SALMON)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHRolls.VOLCANO_ROLL)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHTagGen.RAW_TUNA)
						.addUnordered(YHFood.OTORO.item.get())
						.addUnordered(YHTagGen.RAW_TUNA)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHRolls.RAINBOW_ROLL)
						.addOrdered(YHFood.CRAB_ROE.item.get())
						.addUnordered(CommonTags.FOODS_RAW_SALMON)
						.addUnordered(CommonTags.FOODS_SAFE_RAW_FISH)
						.addUnordered(YHTagGen.RAW_TUNA)
						.save(pvd);
			}

			{

				unlock(pvd, new FixedRecipeBuilder(TableBambooBowls.TUTU_CONGEE)::unlockedBy, Items.BAMBOO)
						.result(YHBowl.TUTU_CONGEE.item.get().asItem()).save(pvd);

				// 1.21.1: RICE_POWDER_PORK and KAGUYA_HIME items not yet migrated from YHBowl enum
				// unlock(pvd, new FixedRecipeBuilder(TableBambooBowls.RICE_POWDER_PORK)::unlockedBy, Items.BAMBOO)
				// 		.result(YHFood.RICE_POWDER_PORK.item.get()).save(pvd);
				//
				// unlock(pvd, new FixedRecipeBuilder(TableBambooBowls.KAGUYA_HIME)::unlockedBy, Items.BAMBOO)
				// 		.result(YHFood.KAGUYA_HIME.item.get()).save(pvd);
			}
		}

	}

	private static void food(RegistrateRecipeProvider pvd, YHFood raw, YHFood cooked) {
		pvd.food(DataIngredient.items(raw.item.get()), RecipeCategory.FOOD, cooked.item, 0.1f);
	}

	private static void cookSave(CookingPotRecipeBuilder builder, RecipeOutput out) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(builder.getResult());
		builder.save(out, ResourceLocation.fromNamespaceAndPath("youkaishomecoming", id.getPath()));
	}

	private static void cookSave(CookingPotRecipeBuilder builder, RecipeOutput out, String suffix) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(builder.getResult());
		builder.save(out, ResourceLocation.fromNamespaceAndPath("youkaishomecoming", id.getPath() + suffix));
	}

	private static void cutSave(CuttingBoardRecipeBuilder builder, RecipeOutput out, String name) {
		builder.save(out, ResourceLocation.fromNamespaceAndPath("youkaishomecoming", name));
	}

	private static void cutting(RegistrateRecipeProvider pvd, ItemEntry<?> in, ItemEntry<?> out, int count) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(in),
						Ingredient.of(CommonTags.TOOLS_KNIFE), out, count, 1)
				.build(pvd, in.getId().withSuffix("_cutting"));
	}

	private static void coffee(RecipeOutput cons, YHCoffee coffee, int count, UnaryOperator<CookingPotRecipeBuilder> func) {
		cookSave(func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), count, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(YHItems.COFFEE_POWDER)), cons);

		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), count, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHCoffee.ESPRESSO.item)
						.addIngredient(YHCoffee.ESPRESSO.item))
				.build(cons, coffee.item.getId().withSuffix("_remix").toString());
	}

	private static void cake(RegistrateRecipeProvider pvd, CakeEntry cake) {
		ResourceLocation cakeId = BuiltInRegistries.BLOCK.getKey(cake.block.get());
		cutSave(CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(cake.block.get()),
						Ingredient.of(CommonTags.TOOLS_KNIFE), cake.item.get(), cake.isCake ? 7 : 4),
				pvd, cakeId.getPath());
		if (cake.isCake) {
			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.requires(cake.item.get(), 7)
					.save(pvd, cake.block.getId().withSuffix("_assemble"));
		} else {
			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.pattern("AA").pattern("AA")
					.define('A', cake.item.get())
					.save(pvd, cake.block.getId().withSuffix("_assemble"));
		}
	}

	private static void steaming(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out) {
		var builder = new dev.xkmc.youkaishomecoming.content.pot.steamer.SteamingRecipeBuilder(
				in.toVanilla(),
				out.get(),
				0,
				200
		);
		net.minecraft.resources.ResourceLocation id = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
				pvd.safeId(out.get()).getNamespace(),
				pvd.safeId(out.get()).getPath() + "_from_" + pvd.safeName(in) + "_steaming"
		);
		builder.unlockedBy("has_" + pvd.safeName(in), in.getCriterion(pvd))
				.save(pvd, id);
	}

	private static void drying(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out) {
		drying(pvd, in, out, 200);
	}

	private static void drying(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out, int time) {
		// Use custom recipe builder for drying rack recipes
		var builder = new dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipeBuilder(
				in.toVanilla(),
				out.get(),
				0,
				time
		);
		net.minecraft.resources.ResourceLocation id = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
				pvd.safeId(out.get()).getNamespace(),
				pvd.safeId(out.get()).getPath() + "_from_" + pvd.safeName(in) + "_drying"
		);
		builder.unlockedBy("has_" + pvd.safeName(in), in.getCriterion(pvd))
				.save(pvd, id);
	}

	public static <T extends ItemLike> void cooking(
			RegistrateRecipeProvider pvd, DataIngredient source, RecipeCategory category,
			Supplier<? extends T> result, float experience, int cookingTime, String typeName,
			RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
		// This method is deprecated in favor of using SimpleCookingRecipeBuilder directly with the recipe constructor
		// Kept for compatibility but should not be used for new recipes
		throw new UnsupportedOperationException("Use SimpleCookingRecipeBuilder directly with recipe constructor instead");
	}

	private static RecipeOutput tea(RegistrateRecipeProvider pvd) {
		return new BasePotOutput<>(YHBlocks.KETTLE_RS.get(), pvd);
	}

	private static RecipeOutput coffee(RegistrateRecipeProvider pvd) {
		return new BasePotOutput<>(YHBlocks.MOKA_RS.get(), pvd);
	}

	private static void foodCut(RegistrateRecipeProvider pvd,
								YHFood raw, YHFood cooked,
								YHFood raw_cut, YHFood cooked_cut) {
		food(pvd, raw, cooked);
		food(pvd, raw_cut, cooked_cut);
		cutting(pvd, raw.item, raw_cut.item, 2);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}

}
