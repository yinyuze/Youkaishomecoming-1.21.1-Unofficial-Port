package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.block.food.PotFoodBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.large.LargeCookingPotBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.mid.MidCookingPotBlock;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Locale;

public enum YHPotFood implements ItemLike {
	// Pot foods using short iron pot (2 servings)
	COD_STEW(() -> ModItems.BAKED_COD_STEW.get(), 2),
	SHIRAYUKI(YHFood.SHIRAYUKI, 2),
	HAN_PALACE(YHFood.HAN_PALACE, 2),
	TOFU_CRAB_STEW(YHFood.TOFU_CRAB_STEW, 2),

	// Pot foods using stockpot (4 servings)
	MISO_SOUP(YHBowl.MISO_SOUP, 4),
	SEAFOOD_MISO_SOUP(YHBowl.SEAFOOD_MISO_SOUP, 4),
	MUSHROOM_SOUP(YHBowl.MUSHROOM_SOUP, 4),
	POTATO_SOUP(YHBowl.POTATO_SOUP, 4),
	POWER_SOUP(YHBowl.POWER_SOUP, 4),
	BORSCHT(YHBowl.BORSCHT, 4),
	;

	private static final Logger LOGGER = LoggerFactory.getLogger(YHPotFood.class);

	public final BlockEntry<? extends PotFoodBlock> block;

	YHPotFood(ItemLike bowl, int serve) {
		String name = name().toLowerCase(Locale.ROOT);
		block = (serve == 2 ? serve2(name, bowl) : serve4(name, bowl))
				.properties(p -> p.mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.lang(YHItems.toEnglishName("pot_of_" + name))
				.register();
	}

	@Override
	public Item asItem() {
		return block.asItem();
	}

	private static BlockBuilder<? extends PotFoodBlock, L2Registrate> serve2(String name, ItemLike bowl) {
		return GensokyoLegacy.REGISTRATE.block("pot_of_" + name, p ->
						new PotFoodBlock.Pot2(p, BowlBlock.POT_SHAPE, bowl))
				.blockstate((ctx, pvd) -> MidCookingPotBlock.buildPotFood(ctx, pvd, name))
				.item().properties(p -> p.craftRemainder(YHBlocks.IRON_POT.asItem()))
				.tab(GensokyoLegacy.TAB.key())
				.build()
				.loot((pvd, b) -> PotFoodBlock.buildLoot(pvd, b, YHBlocks.IRON_POT.asItem()));
	}

	private static BlockBuilder<? extends PotFoodBlock, L2Registrate> serve4(String name, ItemLike bowl) {
		return GensokyoLegacy.REGISTRATE.block("pot_of_" + name, p ->
						new PotFoodBlock.Pot4(p, BowlBlock.STOCKPOT_SHAPE, bowl))
				.blockstate((ctx, pvd) -> LargeCookingPotBlock.buildPotFood(ctx, pvd, name))
				.item().properties(p -> p.craftRemainder(YHBlocks.STOCKPOT.asItem()))
				.tab(GensokyoLegacy.TAB.key())
				.build()
				.loot((pvd, b) -> PotFoodBlock.buildLoot(pvd, b, YHBlocks.STOCKPOT.asItem()));
	}

	public static void register() {
		LOGGER.info("YHPotFood.register() called - starting pot food registration");
		int count = 0;
		for (YHPotFood food : values()) {
			LOGGER.info("Registered pot food: {} -> {}", food.name(), food.block.getId());
			count++;
		}
		LOGGER.info("YHPotFood registration complete - registered {} pot foods", count);
	}

}
