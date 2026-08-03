package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public class KettleRecipeCategory implements IRecipeCategory<KettleRecipe> {

	private final IDrawable background;
	private final IDrawable icon;
	protected final IDrawable heatIndicator;
	protected final IDrawableAnimated arrow;

	public KettleRecipeCategory(IGuiHelper helper) {
		ResourceLocation bg = GensokyoLegacy.loc("textures/gui/kettle.png");
		this.background = helper.createDrawable(bg, 29, 16, 116, 56);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHBlocks.KETTLE.asStack());
		this.heatIndicator = helper.createDrawable(bg, 176, 0, 17, 10);
		this.arrow = helper.drawableBuilder(bg, 176, 15, 35, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<KettleRecipe> getRecipeType() {
		return YHJeiPlugin.KETTLE;
	}

	@Override
	public Component getTitle() {
		return YHLangData.JEI_KETTLE.get();
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
	public void setRecipe(IRecipeLayoutBuilder builder, KettleRecipe recipe, IFocusGroup focus) {
		int borderSlotSize = 18;
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 2; col++) {
				int idx = row * 2 + col;
				if (idx < recipe.input.size()) {
					builder.addSlot(RecipeIngredientRole.INPUT, col * borderSlotSize + 6, row * borderSlotSize + 2)
							.addItemStacks(Arrays.asList(recipe.input.get(idx).getItems()));
				}
			}
		}
		if (!recipe.result.isEmpty()) {
			// Show the drink item that corresponds to the output fluid (matches 1.20.1 behavior).
			var handler = dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler.of(recipe.result);
			if (handler instanceof dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidItem drink) {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 11)
						.addItemStack(drink.toStack(recipe.result));
			} else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 11)
						.addFluidStack(recipe.result.getFluid(), recipe.result.getAmount());
			}
		}
	}
}
