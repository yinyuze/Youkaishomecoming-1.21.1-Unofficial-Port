package dev.xkmc.youkaishomecoming.content.pot.basin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class BasinInput implements RecipeInput {

	protected final BasinBlockEntity be;

	public BasinInput(BasinBlockEntity be) {
		this.be = be;
	}

	@Override
	public ItemStack getItem(int slot) {
		return be.items.getItem(slot);
	}

	@Override
	public int size() {
		return be.items.getContainerSize();
	}

}
