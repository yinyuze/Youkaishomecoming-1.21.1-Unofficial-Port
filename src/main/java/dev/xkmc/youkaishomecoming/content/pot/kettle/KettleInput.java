package dev.xkmc.youkaishomecoming.content.pot.kettle;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record KettleInput(SimpleContainer items) implements RecipeInput {

	@Override
	public ItemStack getItem(int slot) {
		return items.getItem(slot);
	}

	@Override
	public int size() {
		return items.getContainerSize();
	}
}
