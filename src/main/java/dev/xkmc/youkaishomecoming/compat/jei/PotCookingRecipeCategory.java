package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.block.food.PotFoodBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotCookingRecipe;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PotCookingRecipeCategory implements IRecipeCategory<PotCookingRecipe<?>> {

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated arrow;
	private final IDrawableAnimated flame;
	private final ItemStack iconStack;

	public PotCookingRecipeCategory(String id, ItemStack icon, IGuiHelper guiHelper) {
		this.iconStack = icon;
		this.background = guiHelper.createBlankDrawable(18 * 5 + 62, 36);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);

		// Create animated arrow and flame
		ResourceLocation backgroundImage = GensokyoLegacy.loc("textures/gui/moka.png");
		this.arrow = guiHelper.drawableBuilder(backgroundImage, 176, 15, 38, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
		this.flame = guiHelper.drawableBuilder(backgroundImage, 176, 0, 17, 15)
				.buildAnimated(300, IDrawableAnimated.StartDirection.TOP, true);
	}

	@Override
	public RecipeType<PotCookingRecipe<?>> getRecipeType() {
		return YHJeiPlugin.COOKING;
	}

	@Override
	public Component getTitle() {
		return YHLangData.JEI_COOKING.get();
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, PotCookingRecipe<?> recipe, IFocusGroup focuses) {
		int x = 0;
		int y = 0;
		var list = compile(recipe.getInput());
		int n = list.size();
		int width = n <= 5 ? n : (n + 1) / 2;
		int xff = 1 + (5 - width) * 9;
		int yff = n <= 5 ? 10 : 1;
		for (var ing : list) {
			builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + xff, y * 18 + yff)
					.setStandardSlotBackground()
					.addItemStacks(List.of(ing));
			x++;
			if (x >= width) {
				x = 0;
				y++;
				xff = (width - n + 5) * 9 + 1;
			}
		}

		var results = new ArrayList<ItemStack>();
		results.add(recipe.getResult());
		if (recipe.getResult().getItem() instanceof BlockItem item && item.getBlock() instanceof PotFoodBlock block) {
			results.add(block.asBowls());
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 131 - (5 - width) * 9, 10)
				.setOutputSlotBackground()
				.addItemStacks(results);
	}


	private List<ItemStack[]> compile(List<Ingredient> list) {
		Int2ObjectLinkedOpenHashMap<ItemStack[]> set = new Int2ObjectLinkedOpenHashMap<>();
		for (var e : list) {
			if (e.isEmpty()) {
				set.put(1, new ItemStack[0]);
				continue;
			}
			var stacks = e.getItems();
			int result = 1;
			for (var stack : stacks) {
				int hash;
				if (stack.isEmpty()) {
					hash = 0;
				} else {
					hash = BuiltInRegistries.ITEM.getId(stack.getItem());
					// In 1.21.1, use DataComponents instead of getTag()
					var components = stack.getComponents();
					if (!components.isEmpty()) {
						hash += components.hashCode() * 15;
					}
				}
				result = 31 * result + hash;
			}
			var old = set.get(result);
			if (old != null) {
				for (var x : old) {
					x.grow(1);
				}
			} else {
				var copy = stacks.clone();
				for (int i = 0; i < copy.length; i++)
					copy[i] = copy[i].copy();
				set.put(result, copy);
			}
		}
		return new ArrayList<>(set.values());
	}

}
