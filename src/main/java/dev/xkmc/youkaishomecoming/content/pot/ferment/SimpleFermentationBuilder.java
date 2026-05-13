package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidHolder;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHSake;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class SimpleFermentationBuilder extends BaseRecipeBuilder<
		SimpleFermentationBuilder,
		SimpleFermentationRecipe,
		FermentationRecipe<?>,
		FermentationDummyContainer
		> {

	public SimpleFermentationBuilder(IYHSake output, int time) {
		this((String) null, output, time);
	}

	public SimpleFermentationBuilder(TagKey<Fluid> input, IYHSake output, int time) {
		this(input.location().toString(), new FluidStack(output.fluid().get().getSource(), 1000), time);
	}


	public SimpleFermentationBuilder(IYHSake input, IYHSake output, int time) {
		this(BuiltInRegistries.FLUID.getKey(input.fluid().get().getSource()).toString(), new FluidStack(output.fluid().get().getSource(), 1000), time);
	}

	// 新增：支持IYHFluidHolder（YHDrink枚举）
	public SimpleFermentationBuilder(IYHFluidHolder input, IYHFluidHolder output, int time) {
		super(YHBlocks.FERMENT_RS.get(), Items.AIR);
		recipe.inputFluid = new FluidStack(input.source(), 1000);
		recipe.outputFluid = new FluidStack(output.source(), 1000);
		recipe.time = time;
	}

	public SimpleFermentationBuilder(TagKey<Fluid> input, IYHFluidHolder output, int time) {
		this(input.location().toString(), new FluidStack(output.source(), 1000), time);
	}

	public SimpleFermentationBuilder(String inputTag, IYHSake output, int time) {
		this(inputTag, new FluidStack(output.fluid().get().getSource(), 1000), time);
	}

	public SimpleFermentationBuilder(String inputTag, FluidStack output, int time) {
		super(YHBlocks.FERMENT_RS.get(), Items.AIR);
		recipe.inputFluidTag = inputTag;
		recipe.outputFluid = output;
		recipe.time = time;
	}

	public SimpleFermentationBuilder addInput(ItemLike item) {
		return addInput(Ingredient.of(item));
	}

	public SimpleFermentationBuilder addInput(TagKey<Item> item) {
		return addInput(Ingredient.of(item));
	}

	public SimpleFermentationBuilder addInput(Ingredient ing) {
		recipe.ingredients.add(ing);
		return this;
	}

	public SimpleFermentationBuilder addOutput(ItemLike stack) {
		return addOutput(stack.asItem().getDefaultInstance());
	}

	public SimpleFermentationBuilder addOutput(ItemStack stack) {
		recipe.results.add(stack);
		return this;
	}

	@Override
	public void save(RecipeOutput recipeOutput) {
		this.save(recipeOutput, BuiltInRegistries.FLUID.getKey(recipe.outputFluid.getFluid()));
	}
}
