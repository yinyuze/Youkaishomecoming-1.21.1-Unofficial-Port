package dev.xkmc.youkaishomecoming.content.block.donation;

import dev.xkmc.youkaishomecoming.content.attachment.character.CharDataHolder;
import dev.xkmc.youkaishomecoming.content.attachment.character.CharacterData;
import dev.xkmc.youkaishomecoming.content.attachment.character.ReputationState;
import dev.xkmc.youkaishomecoming.content.attachment.datamap.BedData;
import dev.xkmc.youkaishomecoming.content.block.base.IDebugInfoBlockEntity;
import dev.xkmc.youkaishomecoming.content.block.base.LocatedBlockEntity;
import dev.xkmc.youkaishomecoming.content.client.debug.BlockInfoToClient;
import dev.xkmc.youkaishomecoming.content.entity.characters.maiden.MaidenEntity;
import dev.xkmc.youkaishomecoming.event.ReimuEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.registrate.GLEntities;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@SerialClass
public class DonationBoxBlockEntity extends LocatedBlockEntity implements IDebugInfoBlockEntity {

	private static final int SUMMON_COST = 8;

	@SerialField
	private final HashMap<UUID, Integer> donationMap = new HashMap<>();
	@SerialField
	private final HashMap<UUID, Integer> rewardedMap = new HashMap<>();

	public DonationBoxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void take(@Nullable Player player, ItemStack stack) {
		if (player == null) return;
		var bed = BedData.of(getBlockState().getBlock());
		if (bed == null) return;

		int value = 0;
		boolean isSummonItem = false;
		if (stack.is(Items.EMERALD)) { value = 3; isSummonItem = true; }
		else if (stack.is(Items.GOLD_INGOT)) { value = 9; isSummonItem = true; }
		else if (stack.is(Items.GOLD_NUGGET)) value = 1;
		else if (stack.is(Items.GOLD_BLOCK)) value = 81;
		if (value == 0) return;

		int consumed = 0;

		// reputation gain (requires structure binding)
		if (key != null) {
			var holder = CharDataHolder.getUnbounded(player, bed.type());
			int current = holder.data().reputation;
			int max = CharacterData.MAX;
			if (current < max) {
				consumed = Math.min(stack.getCount(), (max - current - 1) / value + 1);
				holder.gain(value * consumed, max);
			}
		}

		// summoning: each emerald/gold_ingot counts as 1 donation unit regardless of reputation
		if (isSummonItem && level instanceof ServerLevel sl && bed.type() == GLEntities.REIMU.get()) {
			if (consumed == 0) consumed = 1;
			UUID uuid = player.getUUID();
			int donated = donationMap.getOrDefault(uuid, 0) + 1;
			int rewarded = rewardedMap.getOrDefault(uuid, 0);
			donationMap.put(uuid, donated);
			if (donated - rewarded >= SUMMON_COST) {
				rewardedMap.put(uuid, rewarded + SUMMON_COST);
				trySummonReimu(sl, player);
			}
			setChanged();
		}

		if (consumed > 0) stack.shrink(consumed);
	}

	private void trySummonReimu(ServerLevel sl, Player player) {
		// if Reimu already exists within 32 blocks, teleport her here instead
		var list = sl.getEntities(EntityTypeTest.forClass(MaidenEntity.class),
				player.getBoundingBox().inflate(32), LivingEntity::isAlive);
		Vec3 dest = Vec3.atCenterOf(getBlockPos().above());
		if (!list.isEmpty()) {
			for (var e : list) {
				e.moveTo(dest);
			}
		} else {
			var maiden = ReimuEventHandlers.trySummon(GLEntities.REIMU, sl, player, 5, 5);
			if (maiden != null) {
				maiden.initSpellCard();
				sl.addFreshEntity(maiden);
				maiden.moveTo(dest);
			}
		}
	}

	@Override
	public BlockInfoToClient getDebugPacket(ServerPlayer player) {
		var bed = BedData.of(getBlockState().getBlock());
		if (bed == null || key == null)
			return BlockInfoToClient.of(GLLang.INFO$BED_UNBOUND.get().withStyle(ChatFormatting.RED));
		var reputation = CharDataHolder.getUnbounded(player, bed.type()).data().reputation;
		return BlockInfoToClient.of(ReputationState.toInfo(reputation));
	}

}
