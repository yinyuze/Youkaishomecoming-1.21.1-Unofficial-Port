package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.youkaishomecoming.content.block.food.EmptySaucerBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseChestBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseFeastBlock;
import dev.xkmc.youkaishomecoming.content.item.character.*;
import dev.xkmc.youkaishomecoming.content.item.ingredient.FairyIceItem;
import dev.xkmc.youkaishomecoming.content.item.ingredient.FrozenFrogItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledFluid;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottleTexture;
import dev.xkmc.youkaishomecoming.content.item.fluid.FlaskItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.util.DCFluid;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.food.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class YHItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with", "in");

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static final BlockEntry<Block> SOYBEAN_BAG, REDBEAN_BAG, COFFEE_BEAN_BAG,
			TEA_BAG, BLACK_TEA_BAG, GREEN_TEA_BAG, OOLONG_TEA_BAG, WHITE_TEA_BAG, PODS_CRATE;

	public static final ItemEntry<StrawHatItem> STRAW_HAT;
	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<KoishiHatItem> KOISHI_HAT;
	public static final ItemEntry<RumiaHairbandItem> RUMIA_HAIRBAND;
	public static final ItemEntry<ReimuHairbandItem> REIMU_HAIRBAND;
	public static final ItemEntry<CirnoHairbandItem> CIRNO_HAIRBAND;
	public static final ItemEntry<CirnoWingsItem> CIRNO_WINGS;

	public static final BottledFluid<SakeBottleItem> SOY_SAUCE_BOTTLE;
	public static final BottledFluid<SakeBottleItem> MAYONNAISE;
	public static final BottledFluid<dev.xkmc.youkaishomecoming.content.item.misc.BloodBottleItem> BLOOD_BOTTLE;
	public static final ItemEntry<SlipBottleItem> SAKE_BOTTLE;
	public static final ItemEntry<FlaskItem> SOY_SAUCE_FLASK, MAYONNAISE_FLASK;
	public static final ItemEntry<Item> CLAY_SAUCER,
			COFFEE_BEAN, COFFEE_POWDER, CREAM, MATCHA,
			STRIPPED_MANDRAKE_ROOT, DRIED_MANDRAKE_FLOWER, ICE_CUBE,
			RAW_BUN, RAW_OYAKI, EMPTY_HAND_ICON;
	public static final ItemEntry<FairyIceItem> FAIRY_ICE_CRYSTAL;
	public static final ItemEntry<FrozenFrogItem> FROZEN_FROG_COLD, FROZEN_FROG_WARM, FROZEN_FROG_TEMPERATE;

	public static final BlockEntry<SurpriseChestBlock> SURP_CHEST;
	public static final BlockEntry<SurpriseFeastBlock> SURP_FEAST;
	public static final CakeEntry TARTE_LUNE, RED_VELVET;
	public static final BlockEntry<EmptySaucerBlock> SAUCER;
	public static final ItemEntry<MobBucketItem> LAMPREY_BUCKET, TUNA_BUCKET, CRAB_BUCKET;
	public static final ItemEntry<Item> CAN;

	public static final DCReg DC = DCReg.of(GensokyoLegacy.REG);
	public static final DCVal<Integer> WATER = DC.intVal("water");
	public static final DCVal<DCFluid> DC_FLUID = DC.reg("fluid", FluidStack.OPTIONAL_CODEC.xmap(DCFluid::new, DCFluid::stack), FluidStack.OPTIONAL_STREAM_CODEC.map(DCFluid::new, DCFluid::stack), true);

	static {
		var reg = GensokyoLegacy.REGISTRATE;

		// plants
		{
			YHCrops.register();
			COFFEE_BEAN = crop("coffee_beans", Item::new);
			COFFEE_POWDER = crop("coffee_powder", Item::new);
			YHTea.register();
			MATCHA = crop("matcha", Item::new);
			STRIPPED_MANDRAKE_ROOT = crop("stripped_mandrake_root", Item::new);
			DRIED_MANDRAKE_FLOWER = crop("dried_mandrake_flower", Item::new);
			SOYBEAN_BAG = YHCrops.SOYBEAN.createBag();
			REDBEAN_BAG = YHCrops.REDBEAN.createBag();
			COFFEE_BEAN_BAG = YHCrops.createBag("coffee_bean");
			TEA_BAG = YHCrops.createBag("tea_leaf");
			BLACK_TEA_BAG = YHTea.BLACK.createBags();
			GREEN_TEA_BAG = YHTea.GREEN.createBags();
			OOLONG_TEA_BAG = YHTea.OOLONG.createBags();
			WHITE_TEA_BAG = YHTea.WHITE.createBags();
			PODS_CRATE = YHCrops.createBag("pods");
		}

		// ingredients
		{
			SOY_SAUCE_BOTTLE = new BottledFluid<>("soy_sauce", "soy_sauce_bottle",
					() -> Items.GLASS_BOTTLE, "ingredient", SakeBottleItem::new);
			MAYONNAISE = new BottledFluid<>("mayonnaise", "mayonnaise_bottle",
					() -> Items.GLASS_BOTTLE, "ingredient", SakeBottleItem::new);
			BLOOD_BOTTLE = new BottledFluid<>("blood", "blood_bottle", "blood",
					() -> Items.GLASS_BOTTLE, "ingredient", dev.xkmc.youkaishomecoming.content.item.misc.BloodBottleItem::new);
			SAKE_BOTTLE = reg.item("sake_bottle", SlipBottleItem::new)
					.properties(p -> p.stacksTo(1))
					.model(BottleTexture::buildBottleModel)
					.color(() -> () -> SlipBottleItem::color)
					.lang("Flask")
					.register();

			// Large flasks (20 uses instead of 4)
			SOY_SAUCE_FLASK = reg.item("soy_sauce_flask", p -> new FlaskItem(p.stacksTo(1), SOY_SAUCE_BOTTLE))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/bottle/ingredient/" + ctx.getName())))
					.color(() -> () -> FlaskItem::color)
					.lang("Large Flask of Soy Sauce")
					.register();

			MAYONNAISE_FLASK = reg.item("mayonnaise_flask", p -> new FlaskItem(p.stacksTo(1), MAYONNAISE))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/bottle/ingredient/" + ctx.getName())))
					.color(() -> () -> FlaskItem::color)
					.lang("Large Flask of Mayonnaise")
					.register();

			CREAM = reg
					.item("bowl_of_cream", p -> new Item(p.craftRemainder(Items.BOWL)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.lang("Bowl of Cream")
					.register();
			ICE_CUBE = ingredient("ice_cube", Item::new);
			RAW_BUN = ingredient("raw_bun", Item::new);
			RAW_OYAKI = ingredient("raw_oyaki", Item::new);
			CAN = reg.item("can", Item::new).register();
			EMPTY_HAND_ICON = reg.item("empty_hand_icon", Item::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + ctx.getName())))
					.register();

			FAIRY_ICE_CRYSTAL = reg
					.item("fairy_ice_crystal", FairyIceItem::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.register();

			FROZEN_FROG_COLD = reg
					.item("frozen_frog_cold", p -> new FrozenFrogItem(p, net.minecraft.world.entity.animal.FrogVariant.COLD))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.register();
			FROZEN_FROG_WARM = reg
					.item("frozen_frog_warm", p -> new FrozenFrogItem(p, net.minecraft.world.entity.animal.FrogVariant.WARM))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.register();
			FROZEN_FROG_TEMPERATE = reg
					.item("frozen_frog_temperate", p -> new FrozenFrogItem(p, net.minecraft.world.entity.animal.FrogVariant.TEMPERATE))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.register();
		}


		YHDrink.register();
		YHFood.register();

		// feasts
		{

			SURP_CHEST = reg.block("chest_of_heart_throbbing_surprise", p ->
							new SurpriseChestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL)))
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName()))).build()
					.blockstate(SurpriseChestBlock::buildModel)
					.register();

			SURP_FEAST = reg.block("heart_throbbing_surprise", p ->
							new SurpriseFeastBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL),
									YHFood.BOWL_OF_HEART_THROBBING_SURPRISE.item))
					.blockstate(SurpriseFeastBlock::buildModel)
					.loot(SurpriseFeastBlock::builtLoot)
					.register();

			TARTE_LUNE = new CakeEntry(reg, "tarte_lune", MapColor.COLOR_PURPLE, FoodType.SIMPLE, 4, 0.6f, false);
			RED_VELVET = new CakeEntry(reg, "red_velvet", MapColor.COLOR_RED, FoodType.SIMPLE, 4, 0.6f, false);
		}

		CLAY_SAUCER = reg.item("clay_saucer", Item::new).register();

		SAUCER = reg.block("saucer", p -> new EmptySaucerBlock(
						BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_WOOL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(),
						state -> state.getValue(EmptySaucerBlock.TYPE).build(pvd)))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.register();

		YHDish.register();
		YHCoffee.register();
		// YHSake.register(); // Removed: YHSake items are now in YHDrink to avoid duplicate registration

		// curio items - registered in GLItems to avoid duplicate registration
		{
			STRAW_HAT = GLItems.STRAW_HAT;
			SUWAKO_HAT = GLItems.SUWAKO_HAT;
			KOISHI_HAT = GLItems.KOISHI_HAT;
			RUMIA_HAIRBAND = GLItems.RUMIA_HAIRBAND;
			REIMU_HAIRBAND = GLItems.REIMU_HAIRBAND;
			CIRNO_HAIRBAND = GLItems.CIRNO_HAIRBAND;
			CIRNO_WINGS = GLItems.CIRNO_WINGS;
		}

		//if (ModList.get().isLoaded(FruitsDelight.MODID)) {
		//	FruitsDelightCompatFood.register();
		//}

		LAMPREY_BUCKET = reg
				.item("lamprey_bucket", p -> new MobBucketItem(
						YHEntities.LAMPREY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();

		TUNA_BUCKET = reg
				.item("tuna_bucket", p -> new MobBucketItem(
						GLEntities.TUNA.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();

		CRAB_BUCKET = reg
				.item("crab_bucket", p -> new MobBucketItem(
						GLEntities.CRAB.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();
	}

	public static <T extends Item> ItemEntry<T> seed(String id, NonNullFunction<Item.Properties, T> factory) {
		return GensokyoLegacy.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/crops/" + ctx.getName())))
				.tag(Tags.Items.SEEDS)
				.register();
	}

	public static <T extends Item> ItemEntry<T> crop(String id, NonNullFunction<Item.Properties, T> factory) {
		return GensokyoLegacy.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/crops/" + ctx.getName())))
				.register();
	}

	public static <T extends Item> ItemEntry<T> ingredient(String id, NonNullFunction<Item.Properties, T> factory) {
		return GensokyoLegacy.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
	}

	public static void register() {
		dev.xkmc.youkaishomecoming.init.food.YHFood.register();
		dev.xkmc.youkaishomecoming.init.food.YHDish.register();
		dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi.register();
		dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls.init();
	}

}
