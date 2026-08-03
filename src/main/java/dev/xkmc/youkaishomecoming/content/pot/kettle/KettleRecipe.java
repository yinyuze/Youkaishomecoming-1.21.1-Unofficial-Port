package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SerialClass
public class KettleRecipe extends BaseRecipe<KettleRecipe, KettleRecipe, KettleInput> implements TimedRecipe {

	@SerialField
	public final List<Ingredient> input = new ArrayList<>();
	@SerialField
	public int time;
	@SerialField
	public FluidStack result = FluidStack.EMPTY;

	public KettleRecipe() {
		super(YHBlocks.KETTLE_RS.get());
	}

	@Override
	public boolean matches(KettleInput cont, Level level) {
		SimpleContainer items = cont.items();
		Set<ItemStack> available = new LinkedHashSet<>();
		for (int i = 0; i < items.getContainerSize(); i++) {
			var e = items.getItem(i);
			if (e.isEmpty()) continue;
			available.add(e);
		}
		for (var ing : input) {
			ItemStack match = null;
			for (var e : available) {
				if (ing.test(e)) {
					match = e;
					break;
				}
			}
			if (match == null) {
				return false;
			}
			available.remove(match);
		}
		return available.isEmpty();
	}

	@Override
	public ItemStack assemble(KettleInput cont, HolderLookup.Provider access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getProcessTime() {
		return time;
	}
}
