package dev.xkmc.youkaishomecoming.content.pot.kettle;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;

@SuppressWarnings("deprecation")
public class KettleBlock extends BaseEntityBlock {

	public static final MapCodec<KettleBlock> CODEC = simpleCodec(KettleBlock::new);

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<CookingPotSupport> SUPPORT = CookingPotBlock.SUPPORT;

	protected static final VoxelShape SHAPE = box(3, 0, 3, 13, 7, 13);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0));

	public KettleBlock(Properties prop) {
		super(prop);
		registerDefaultState(defaultBlockState().setValue(SUPPORT, CookingPotSupport.NONE));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SUPPORT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
		return context.getClickedFace().equals(Direction.DOWN) ?
				state.setValue(SUPPORT, CookingPotSupport.HANDLE) :
				state.setValue(SUPPORT, getTrayState(level, pos));
	}

	@Override
	public BlockState updateShape(BlockState current, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return facing.getAxis().equals(Direction.Axis.Y) &&
				!current.getValue(SUPPORT).equals(CookingPotSupport.HANDLE) ?
				current.setValue(SUPPORT, getTrayState(level, currentPos)) : current;
	}

	private CookingPotSupport getTrayState(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES) ? CookingPotSupport.TRAY : CookingPotSupport.NONE;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity user, ItemStack stack) {
		if (level.getBlockEntity(pos) instanceof KettleBlockEntity be) {
			be.readFromStack(stack);
		}
	}

	// Left-click packaging is handled by GeneralEventHandlers.onLeftClickBlock, which dispatches
	// to KettleBlockEntity#leftClick. No Block#attack override needed.

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof KettleBlockEntity be)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (stack.isEmpty()) {
			if (!level.isClientSide()) {
				if (player.isShiftKeyDown()) {
					be.dumpInventory();
				} else {
					level.setBlockAndUpdate(pos, state.setValue(SUPPORT,
							state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE)
									? getTrayState(level, pos) : CookingPotSupport.HANDLE));
					level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
				}
			}
			return ItemInteractionResult.SUCCESS;
		}
		if (stack.getItem() instanceof YHDrinkItem && !(stack.getItem().components().has(net.minecraft.core.component.DataComponents.FOOD))) {
			return FluidItemTile.addItem(be, stack, level, pos);
		}
		return FluidItemTile.addFluidOrItem(be, stack, level, pos, player, hand, hit);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, list, flag);
		var data = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
		if (data != null) {
			var root = data.copyTag();
			if (root.contains("KettleFluid", Tag.TAG_COMPOUND)) {
				FluidStack fluid = FluidStack.parseOptional(ctx.registries(), root.getCompound("KettleFluid"));
				if (!fluid.isEmpty() && YHFluidHandler.of(fluid) instanceof IYHFluidItem drink)
					list.add(drink.toStack(fluid).getHoverName());
			}
		}
		list.add(YHLangData.KETTLE_INFO.get());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (!(level.getBlockEntity(pos) instanceof KettleBlockEntity be)) return;
		if (!be.isHeated(level, pos)) return;
		double x = pos.getX() + 0.5;
		double y = pos.getY();
		double z = pos.getZ() + 0.5;
		if (random.nextInt(10) == 0) {
			SoundEvent boilSound = ModSounds.BLOCK_COOKING_POT_BOIL.get();
			level.playLocalSound(x, y, z, boilSound, net.minecraft.sounds.SoundSource.BLOCKS,
					0.5F, random.nextFloat() * 0.2F + 0.9F, false);
		}
		if (random.nextFloat() < 0.2F) {
			double px = x + (random.nextDouble() * 0.4 - 0.2);
			double py = y + 0.5;
			double pz = z + (random.nextDouble() * 0.4 - 0.2);
			level.addParticle(ParticleTypes.BUBBLE_POP, px, py, pz, 0.0, 0.0, 0.0);
		}
		if (random.nextFloat() < 0.05F) {
			double px = x + (random.nextDouble() * 0.3 - 0.15);
			double py = y + 0.5;
			double pz = z + (random.nextDouble() * 0.3 - 0.15);
			double motionY = random.nextBoolean() ? 0.015 : 0.005;
			level.addParticle(ModParticleTypes.STEAM.get(), px, py, pz, 0.0, motionY, 0.0);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return YHBlocks.KETTLE_BE.get().create(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, YHBlocks.KETTLE_BE.get(), (l, p, s, be) -> be.tick());
	}

	public static void buildModel(DataGenContext<Block, KettleBlock> ctx, RegistrateBlockstateProvider pvd) {
		var kettle = pvd.models().getBuilder("block/kettle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/kettle")))
				.texture("kettle", pvd.modLoc("block/utensil/kettle"))
				.renderType("cutout");
		var handle = pvd.models().getBuilder("block/kettle_handle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/kettle_handle")))
				.texture("kettle", pvd.modLoc("block/utensil/kettle"))
				.texture("handle", pvd.modLoc("block/utensil/cooking_pot_handle"))
				.texture("chain", pvd.modLoc("block/utensil/chain"))
				.renderType("cutout");
		var tray = pvd.models().getBuilder("block/kettle_tray")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/kettle_tray")))
				.texture("kettle", pvd.modLoc("block/utensil/kettle"))
				.texture("tray_side", pvd.modLoc("block/utensil/cooking_pot_tray_side"))
				.texture("tray_top", pvd.modLoc("block/utensil/cooking_pot_tray_top"))
				.renderType("cutout");
		pvd.horizontalBlock(ctx.get(), state -> switch (state.getValue(SUPPORT)) {
			case NONE -> kettle;
			case HANDLE -> handle;
			case TRAY -> tray;
		});
	}
}
