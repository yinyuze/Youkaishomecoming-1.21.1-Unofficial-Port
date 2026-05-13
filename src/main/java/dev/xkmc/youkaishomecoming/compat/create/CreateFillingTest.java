package dev.xkmc.youkaishomecoming.compat.create;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;
import java.util.function.Supplier;

public record CreateFillingTest(Supplier<ItemStack> result, int amount) {

	public static Optional<CreateFillingTest> test(Level level, FluidStack fluid, ItemStack bottle) {
		// Create mod integration disabled - requires Create mod to be present
		return Optional.empty();
	}

}
