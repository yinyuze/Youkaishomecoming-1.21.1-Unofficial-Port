package dev.xkmc.youkaishomecoming.content.pot.rack;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.LinkedHashMap;
import java.util.Map;

public class DryingRackRecipeBuilder {
	private final Ingredient ingredient;
	private final ItemStack result;
	private final float experience;
	private final int cookingTime;
	private final Map<String, Criterion<InventoryChangeTrigger.TriggerInstance>> criteria = new LinkedHashMap<>();

	public DryingRackRecipeBuilder(Ingredient ingredient, ItemLike result, float experience, int cookingTime) {
		this.ingredient = ingredient;
		this.result = new ItemStack(result);
		this.experience = experience;
		this.cookingTime = cookingTime;
	}

	public DryingRackRecipeBuilder unlockedBy(String name, Criterion<InventoryChangeTrigger.TriggerInstance> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	public void save(RecipeOutput output, ResourceLocation id) {
		var recipe = new DryingRackRecipe("", net.minecraft.world.item.crafting.CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
		var builder = output.advancement()
				.addCriterion("has_the_recipe", net.minecraft.advancements.critereon.RecipeUnlockedTrigger.unlocked(id))
				.rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(id))
				.requirements(net.minecraft.advancements.AdvancementRequirements.Strategy.OR);

		this.criteria.forEach(builder::addCriterion);

		output.accept(id, recipe, builder.build(id.withPrefix("recipes/misc/")));
	}
}
