package dev.xkmc.youkaishomecoming.content.block.food;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItemClient;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

public class FleshFeastBlock extends FeastBlock {

	private static final int[] HEIGHT = {4, 5, 6, 7, 9};
	protected static final VoxelShape[] SHAPE_BY_BITE;

	static {
		SHAPE_BY_BITE = new VoxelShape[5];
		for (int i = 0; i < 5; i++) {
			SHAPE_BY_BITE[i] = Shapes.or(
					box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0),
					Shapes.join(box(4.0, 2.0, 4.0, 12.0, 8.0, 12.0),
							box(5.0, 2.0, 5.0, 11.0, 9.0, 11.0), BooleanOp.ONLY_FIRST),
					box(5.0, 2.0, 5.0, 11.0, HEIGHT[i], 11.0)
			);
		}
	}

	public FleshFeastBlock(Properties properties, Supplier<Item> servingItem) {
		super(properties, servingItem, true);
	}

	@Override
	public MutableComponent getName() {
		Component name = FMLEnvironment.dist == Dist.CLIENT
				? FleshFoodItemClient.getFleshName()
				: GLLang.FLESH$FLESH_HUMAN.get();
		return Component.translatable(getDescriptionId(), name);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE_BY_BITE[state.getValue(SERVINGS)];
	}

	public static void builtLoot(RegistrateBlockLootTables pvd, FleshFeastBlock block) {
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem())
						.when(ExplosionCondition.survivesExplosion())
						.when(getServe(block))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.BOWL))
						.when(ExplosionCondition.survivesExplosion())
						.when(InvertedLootItemCondition.invert(getServe(block))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.SKELETON_SKULL))
						.when(ExplosionCondition.survivesExplosion())
						.when(InvertedLootItemCondition.invert(getServe(block)))));
	}

	private static <T extends FeastBlock> LootItemBlockStatePropertyCondition.Builder getServe(T block) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
				.setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(block.getServingsProperty(), block.getMaxServings()));
	}

}
