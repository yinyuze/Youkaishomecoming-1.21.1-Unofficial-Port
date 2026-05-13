package dev.xkmc.youkaishomecoming.init.loot;

import com.mojang.serialization.MapCodec;
import dev.xkmc.youkaishomecoming.compat.curios.CuriosCompat;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.GLMisc;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

public record KilledByRumiaHairbandCondition() implements LootItemCondition {

	public static final MapCodec<KilledByRumiaHairbandCondition> CODEC =
			MapCodec.unit(new KilledByRumiaHairbandCondition());

	public static final KilledByRumiaHairbandCondition INSTANCE = new KilledByRumiaHairbandCondition();

	@Override
	public boolean test(LootContext ctx) {
		var attacker = ctx.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
		if (!(attacker instanceof LivingEntity le)) return false;
		ItemStack head = le.getItemBySlot(EquipmentSlot.HEAD);
		if (head.is(GLItems.RUMIA_HAIRBAND.asItem())) return true;
		if (attacker instanceof Player player && ModList.get().isLoaded("curios")) {
			return CuriosCompat.hasCurioItem(player, GLItems.RUMIA_HAIRBAND.asItem());
		}
		return false;
	}

	@NotNull
	@Override
	public LootItemConditionType getType() {
		return GLMisc.KILLED_BY_RUMIA.get();
	}

}
