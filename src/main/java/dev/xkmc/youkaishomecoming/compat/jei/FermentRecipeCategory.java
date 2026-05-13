package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FermentRecipeCategory extends BaseRecipeCategory<SimpleFermentationRecipe, FermentRecipeCategory> {
	protected static final ResourceLocation BG = GensokyoLegacy.loc("textures/gui/ferment.png");

	public FermentRecipeCategory() {
		super(GensokyoLegacy.loc("ferment"), SimpleFermentationRecipe.class);
	}

	public FermentRecipeCategory init(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(BG, 0, 0, 144, 54);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHBlocks.FERMENT.asStack());
		return this;
	}

	public Component getTitle() {
		return YHLangData.JEI_FERMENT.get();
	}

	public void setRecipe(IRecipeLayoutBuilder builder, SimpleFermentationRecipe recipe, IFocusGroup focuses) {
		int n = 0;
		for (var stack : recipe.ingredients) {
			if (stack.isEmpty()) continue;
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 1;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(stack);
			n++;
		}
		// Handle inputFluid (FluidStack-based input, used by wine recipes)
		if (!recipe.inputFluid.isEmpty()) {
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 1;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(NeoForgeTypes.FLUID_STACK, List.of(recipe.inputFluid));
			n++;
		}
		// Handle inputFluidTag (tag-based input, used by sake recipes)
		else if (recipe.inputFluidTag != null) {
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 1;
			// Create FluidStack from tag for JEI display
			var tagKey = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.BuiltInRegistries.FLUID.key(), net.minecraft.resources.ResourceLocation.parse(recipe.inputFluidTag));
			var fluids = net.minecraft.core.registries.BuiltInRegistries.FLUID.getTag(tagKey);
			if (fluids.isPresent()) {
				var fluidStacks = fluids.get().stream()
					.map(holder -> new net.neoforged.neoforge.fluids.FluidStack(holder.value(), 1000))
					.toList();
				if (!fluidStacks.isEmpty()) {
					builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(NeoForgeTypes.FLUID_STACK, fluidStacks);
					n++;
				}
			}
		}
		n = 0;
		for (var stack : recipe.results) {
			if (stack.isEmpty()) continue;
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 91;
			builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(stack);
			n++;
		}
		if (!recipe.outputFluid.isEmpty()) {
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 91;
			// Handle SakeFluid (old YHSake enum)
			if (recipe.outputFluid.getFluid() instanceof SakeFluid sake) {
				builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(sake.type.asStack(sake.type.count()));
				builder.addSlot(RecipeIngredientRole.INPUT, 64, 1).addItemStack(new ItemStack(sake.type.getContainer(), sake.type.count()));
			}
			// Handle YHFluid (new YHDrink enum) - display as item instead of fluid
			else if (recipe.outputFluid.getFluid() instanceof dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid yhFluid) {
				var holder = yhFluid.type;
				// Display the drink item (bottle) instead of fluid
				builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(holder.item().asStack(1));
				// Display the container needed (glass bottle, bamboo, etc.)
				if (holder instanceof dev.xkmc.youkaishomecoming.init.food.YHDrink drink) {
					var container = drink.getContainer();
					builder.addSlot(RecipeIngredientRole.INPUT, 64, 1).addItemStack(new ItemStack(container, 1));
				}
			}
			// Fallback: display as fluid
			else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addIngredients(NeoForgeTypes.FLUID_STACK, List.of(recipe.outputFluid));
			}
		}
	}
}
