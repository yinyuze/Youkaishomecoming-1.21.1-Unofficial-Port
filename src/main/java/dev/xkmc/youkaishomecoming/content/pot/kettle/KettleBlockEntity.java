package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2core.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static net.minecraft.world.level.block.Block.popResource;

@SerialClass
public class KettleBlockEntity extends BaseBlockEntity
		implements TickableBlockEntity, BaseContainerListener, BlockContainer,
		HeatableBlockEntity, LeftClickBlock, FluidItemTile, InfoTile {

	@SerialField
	public final KettleContainer items = new KettleContainer(4).setMax(1).add(this);

	@SerialField
	public final BaseTank fluids = new BaseTank(1, 1000).add(this);

	@SerialField
	private int heat = 0;

	@SerialField
	protected int totalTime = 0, recipeProgress = 0;

	@SerialField
	@Nullable
	protected net.minecraft.resources.ResourceLocation recipeId = null;

	private boolean doRecipeSearch = true;
	private RecipeHolder<KettleRecipe> recipe = null;

	private final IItemHandler itemHandler = new InvWrapper(items);

	public KettleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public BaseTank getFluidHandler() {
		return fluids;
	}

	@Override
	public SimpleContainer getItemHandler() {
		return items;
	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	public void notifyTile() {
		setChanged();
		sync();
		doRecipeSearch = true;
	}

	public float inProgress() {
		if (totalTime == 0) return 0;
		float f = 1f * recipeProgress / totalTime;
		return f < 0 ? 0 : (f > 1 ? 1 : f);
	}

	public int getHeat() {
		return heat;
	}

	@Override
	public void tick() {
		if (level == null) return;
		// heat update
		if (!level.isClientSide()) {
			var fluid = fluids.getFluidInTank(0);
			int prevHeat = heat;
			if (fluid.getFluid().is(FluidTags.WATER)) {
				if (heat >= fluid.getAmount()) {
					heat = fluid.getAmount();
				} else {
					if (isHeated(level, getBlockPos())) heat++;
				}
			} else if (heat != 0) {
				heat = 0;
			}
			// Periodic sync so client-side tooltip (Heating: X%) updates without needing a block-state change.
			if (heat != prevHeat && (heat % 10 == 0 || heat == 0 || heat == fluid.getAmount())) {
				sync();
			}
		}
		// recipe machinery, mirrors TimedRecipeBlockEntity
		if (level.isClientSide()) {
			if (totalTime > 0) {
				if (shouldStopProcessing()) {
					if (recipeProgress > 0) recipeProgress--;
				} else {
					recipeProgress++;
				}
			}
			return;
		}
		if (doRecipeSearch) {
			if (!items.isEmpty()) {
				Optional<RecipeHolder<KettleRecipe>> opt = level.getRecipeManager()
						.getRecipeFor(YHBlocks.KETTLE_RT.get(), new KettleInput(items), level);
				if (opt.isPresent()) {
					recipe = opt.get();
					totalTime = recipe.value().getProcessTime();
					if (!recipe.id().equals(recipeId)) {
						recipeProgress = 0;
						recipeId = recipe.id();
					} else if (recipeProgress > totalTime) {
						recipeProgress = totalTime - 1;
					}
				} else {
					recipeId = null;
					recipe = null;
					totalTime = 0;
					recipeProgress = 0;
				}
				sync();
			}
			doRecipeSearch = false;
		}
		if (totalTime > 0) {
			if (shouldStopProcessing()) {
				if (recipeProgress > 0) recipeProgress--;
			} else {
				recipeProgress++;
			}
			if (recipeProgress >= totalTime) {
				if (recipe != null) finishRecipe(recipe.value());
				recipeProgress = 0;
				totalTime = 0;
				recipeId = null;
				recipe = null;
				sync();
			}
		}
	}

	private boolean shouldStopProcessing() {
		if (heat < 1000) return true;
		var stack = fluids.getFluidInTank(0);
		if (stack.getAmount() < 1000) return true;
		return !stack.getFluid().is(FluidTags.WATER);
	}

	private void finishRecipe(KettleRecipe rec) {
		items.clearContent();
		fluids.set(0, 0, rec.result.copy());
		heat = 0;
	}

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), items);
		notifyTile();
	}

	public void readFromStack(ItemStack stack) {
		try {
			CompoundTag root = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA) != null
					? stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag()
					: null;
			if (root == null) return;
			if (root.contains("KettleContents", Tag.TAG_LIST)) {
				ListTag list = root.getList("KettleContents", Tag.TAG_COMPOUND);
				for (Tag e : list) {
					if (e instanceof CompoundTag c) {
						ItemStack it = ItemStack.parseOptional(level == null ? null : level.registryAccess(), c);
						if (!it.isEmpty()) items.addItem(it);
					}
				}
			}
			if (root.contains("KettleFluid", Tag.TAG_COMPOUND)) {
				FluidStack fs = FluidStack.parseOptional(level == null ? null : level.registryAccess(),
						root.getCompound("KettleFluid"));
				fluids.set(0, 0, fs);
			}
			heat = root.getInt("KettleHeat");
		} catch (Exception ignored) {
		}
	}

	@Override
	public boolean leftClick(BlockState state, Level level, BlockPos pos, Player player) {
		// Fall through to vanilla break if any of: client, holding a tool, or kettle is empty.
		if (level.isClientSide) return false;
		if (!player.getMainHandItem().isEmpty()) return false;
		if (items.isEmpty() && fluids.getFluidInTank(0).isEmpty() && heat == 0) return false;
		ItemStack stack = state.getBlock().asItem().getDefaultInstance();
		CompoundTag root = new CompoundTag();
		ListTag list = new ListTag();
		for (ItemStack e : items.getAsList()) {
			if (e.isEmpty()) continue;
			list.add(e.save(level.registryAccess(), new CompoundTag()));
		}
		items.clearContent();
		root.put("KettleContents", list);
		root.put("KettleFluid", fluids.getFluidInTank(0).saveOptional(level.registryAccess()));
		root.put("KettleHeat", IntTag.valueOf(heat));
		stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
				net.minecraft.world.item.component.CustomData.of(root));
		level.removeBlock(pos, false);
		if (player.getMainHandItem().isEmpty()) {
			player.setItemInHand(InteractionHand.MAIN_HAND, stack);
		} else {
			popResource(level, pos, stack);
		}
		return true;
	}

	public void heatUp(int val) {
		var fluid = fluids.getFluidInTank(0);
		if (!fluid.getFluid().is(FluidTags.WATER)) {
			heat = 0;
			return;
		}
		heat = Math.min(fluid.getAmount(), heat + val);
	}

	public void prepareforHotWater(int space) {
		var fluid = fluids.getFluidInTank(0);
		if (!fluid.getFluid().is(FluidTags.WATER)) return;
		if (heat < fluid.getAmount() && fluid.getAmount() + space > 1000) {
			fluid.setAmount(Math.max(heat, 1000 - space));
		}
	}

	public IItemHandler getItemHandlerCap(@Nullable Direction side) {
		return itemHandler;
	}

	public IFluidHandler getFluidHandlerCap(@Nullable Direction side) {
		return fluids;
	}

	@Override
	public TileTooltip getImage(boolean shift, BlockHitResult hit) {
		return new TileTooltip(items.getAsList(), fluids.getAsList());
	}

	@Override
	public List<Component> lines(boolean shift, BlockHitResult hit) {
		var fluid = fluids.getFluidInTank(0);
		if (!fluid.getFluid().is(FluidTags.WATER)) return List.of();
		float prog = inProgress();
		if (prog > 0) return List.of(YHLangData.BREWING_PROGRESS.get(Math.round(prog * 100) + "%"));
		return List.of(YHLangData.HEAT_PROGRESS.get(heat / 10 + "%"));
	}
}
