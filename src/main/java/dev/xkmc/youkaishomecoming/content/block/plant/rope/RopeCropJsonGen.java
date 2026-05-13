package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class RopeCropJsonGen {

	public static void buildRootedModel(DataGenContext<Block, ? extends RootedClimbingCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			boolean rope = state.getValue(RopeLoggedCropBlock.ROPELOGGED);
			boolean base = state.getValue(RootedClimbingCropBlock.BASE);
			int age = state.getValue(CropBlock.AGE);
			String tex = "block/plants/" + name + "/" + (base ? "base" : "vine") + age;
			if (rope) {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("rope_" + name + "_" + (base ? "base" : "vine") + age)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop")))
						.texture("cross", tex)
						.texture("rope_side", pvd.modLoc("block/plants/rope"))
						.texture("rope_top", pvd.modLoc("block/plants/rope_top"))
						.renderType("cutout")
				).build();
			} else {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("bare_" + name + "_" + (base ? "base" : "vine") + age)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop_bare")))
						.texture("cross", tex)
						.renderType("cutout")
				).build();
			}
		});
	}

	public static void buildPlantModel(DataGenContext<Block, ? extends RootedClimbingCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		buildRootedModel(ctx, pvd, name);
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, CropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, block.getMaxAge()))
										.invert())
								.add(LootItem.lootTableItem(crop.getSeed())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, block.getMaxAge())))
								.add(LootItem.lootTableItem(crop.getFruits())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, block.getMaxAge())))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(
												pvd.getRegistries().holderOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))))));
	}

}
