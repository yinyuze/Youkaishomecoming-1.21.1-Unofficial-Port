package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItem;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class FoodType {
	public static final FoodType SIMPLE = new FoodType(YHFoodItem::new, false, false, null);
	public static final FoodType FAST = new FoodType(YHFoodItem::new, true, false, null);
	public static final FoodType MEAT = new FoodType(YHFoodItem::new, false, false, null);
	public static final FoodType MEAT_SLICE = new FoodType(YHFoodItem::new, true, false, null);
	public static final FoodType STICK = new FoodType(YHFoodItem::new, true, false, Items.STICK);
	public static final FoodType BOWL = new FoodType(YHFoodItem::new, false, false, Items.BOWL);
	public static final FoodType IRON_BOWL = new FoodType(YHFoodItem::new, false, false, YHBlocks.IRON_BOWL);
	public static final FoodType BAMBOO_BOWL = new FoodType(YHFoodItem::new, false, false, Items.BAMBOO);
	public static final FoodType SAKE = new FoodType(YHDrinkItem::new, false, true, Items.BOWL);
	public static final FoodType BOTTLE = new FoodType(YHDrinkItem::new, false, true, Items.GLASS_BOTTLE);
	public static final FoodType BAMBOO = new FoodType(YHDrinkItem::new, false, true, Items.BAMBOO);
	public static final FoodType BOTTLE_FAST = new FoodType(YHDrinkItem::new, true, true, Items.GLASS_BOTTLE);
	public static final FoodType BOWL_MEAT = new FoodType(YHFoodItem::new, false, false, Items.BOWL);
	public static final FoodType FLESH = new FoodType(FleshFoodItem::new, false, false, null, YHTagGen.FLESH_FOOD);
	public static final FoodType FLESH_FAST = new FoodType(FleshFoodItem::new, true, false, null, YHTagGen.FLESH_FOOD);
	public static final FoodType BOWL_FLESH = new FoodType(FleshFoodItem::new, false, false, Items.BOWL, YHTagGen.FLESH_FOOD);
	public static final FoodType CAN_FLESH = new FoodType(FleshFoodItem::new, true, false, YHItems.CAN, YHTagGen.FLESH_FOOD);

	private final Function<Item.Properties, Item> factory;
	private final boolean fast;
	private final boolean alwaysEat;
	private final ItemLike container;
	private final TagKey<Item>[] tags;
	private final EffectEntry[] effs;


	@SafeVarargs
	public FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable ItemLike container, EffectEntry[] effs, TagKey<Item>... tags) {
		this.factory = factory;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.container = container;
		this.tags = tags;
		this.effs = effs;
	}

	@SafeVarargs
	public FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable ItemLike container, TagKey<Item>... tags) {
		this(factory, fast, alwaysEat, container, new EffectEntry[0], tags);
	}

	private Item.Properties food(Item.Properties prop, int nutrition, float sat, List<EffectEntry> effs) {
		var food = new FoodProperties.Builder()
				.nutrition(nutrition).saturationModifier(sat);
		if (fast) food.fast();
		if (alwaysEat) food.alwaysEdible();
		for (var e : this.effs) {
			food.effect(e::getEffect, e.chance());
		}
		for (var e : effs) {
			food.effect(e::getEffect, e.chance());
		}
		if (container != null) {
			food.usingConvertsTo(container);
			prop.craftRemainder(container.asItem());
			prop.stacksTo(16);
		}
		prop.food(food.build());
		return prop;
	}

	public Item.Properties food(Item.Properties prop, float edibility, int nutrition, float sat, List<EffectEntry> effs) {
		return edibility <= 0 ? (container != null ? prop.craftRemainder(container.asItem()).stacksTo(16) : prop) :
				edibility < 1 ? food(prop, (int) (nutrition * edibility), sat * edibility, List.of()) :
						food(prop, nutrition, sat, effs);
	}

	public ItemEntry<Item> build(L2Registrate reg, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(reg, factory, folder, name, nutrition, sat, tags, effs);
	}

	public ItemEntry<Item> build(L2Registrate reg, Function<Item.Properties, Item> factory, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		boolean opt = folder.startsWith("?");
		var path = opt ? folder.substring(1) : folder;
		return reg.item(name, p -> factory.apply(food(p, nutrition, sat, effs)))
				.transform(e -> opt ? e.asOptional() : e)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + path + ctx.getName())))
				.tag(getTags(this.tags, tags))
				.lang(Item::getDescriptionId, makeLang(name))
				.register();
	}

	// Convenience methods that use GensokyoLegacy.REGISTRATE
	public com.tterrag.registrate.builders.ItemBuilder<Item, L2Registrate> build(String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(factory, name, nutrition, sat, tags, effs);
	}

	public com.tterrag.registrate.builders.ItemBuilder<Item, L2Registrate> build(Function<Item.Properties, Item> factory, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return dev.xkmc.youkaishomecoming.init.GensokyoLegacy.REGISTRATE
				.item(name, p -> factory.apply(food(p, nutrition, sat, effs)))
				.tag(getTags(this.tags, tags))
				.lang(Item::getDescriptionId, makeLang(name));
	}

	public ItemEntry<Item> build(String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(dev.xkmc.youkaishomecoming.init.GensokyoLegacy.REGISTRATE, folder, name, nutrition, sat, tags, effs);
	}

	public boolean isFlesh() {
		for (var tag : tags) {
			if (tag == YHTagGen.FLESH_FOOD) return true;
		}
		return false;
	}

	public boolean isReimuFood() {
		return !isFlesh();
	}

	public com.tterrag.registrate.builders.BlockBuilder<dev.xkmc.youkaishomecoming.content.block.food.BowlBlock, dev.xkmc.l2core.init.reg.registrate.L2Registrate> bowl(String name, boolean raw) {
		if (this == IRON_BOWL)
			return dev.xkmc.youkaishomecoming.content.block.food.BowlBlock.ironBowlFood(name);
		if (this == BAMBOO_BOWL)
			return raw ? dev.xkmc.youkaishomecoming.content.block.food.BowlBlock.rawBambooBowl(name) : dev.xkmc.youkaishomecoming.content.block.food.BowlBlock.bambooBowl(name);
		return dev.xkmc.youkaishomecoming.content.block.food.BowlBlock.woodBowlFood(name);
	}

	public String makeLang(String id) {
		String name = YHItems.toEnglishName(id.toLowerCase(Locale.ROOT));
		return YHItems.toEnglishName(name);
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private static TagKey<Item>[] getTags(TagKey<Item>[] a, TagKey<Item>[] b) {
		var ans = new ArrayList<>(List.of(a));
		ans.addAll(List.of(b));
		return ans.toArray(TagKey[]::new);
	}
}
