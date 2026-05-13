package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class SteamTrigger extends SimpleCriterionTrigger<SteamTrigger.TriggerInstance> {

	static final ResourceLocation ID = GensokyoLegacy.loc("steam");

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack, int layer) {
		this.trigger(player, (ins) -> ins.matches(stack, layer));
	}

	public static Criterion<TriggerInstance> steam(TagKey<Item> tag) {
		return new SteamTrigger().createCriterion(new TriggerInstance(
				Optional.empty(),
				Optional.of(ItemPredicate.Builder.item().of(tag).build()),
				MinMaxBounds.Ints.ANY));
	}

	public static Criterion<TriggerInstance> steam(MinMaxBounds.Ints layer) {
		return new SteamTrigger().createCriterion(new TriggerInstance(
				Optional.empty(),
				Optional.empty(),
				layer));
	}

	public record TriggerInstance(
			Optional<ContextAwarePredicate> player,
			Optional<ItemPredicate> item,
			MinMaxBounds.Ints rack
	) implements SimpleInstance {

		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("rack", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::rack)
		).apply(inst, TriggerInstance::new));

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		}

		public boolean matches(ItemStack pItem, int layer) {
			return item.map(p -> p.test(pItem)).orElse(true) && rack.matches(layer);
		}
	}
}
