package dev.xkmc.youkaishomecoming.init.loot;

import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLTagGen;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import dev.xkmc.youkaishomecoming.mixin.AddLootTableModifierAccessor;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;

import java.util.concurrent.CompletableFuture;

public class YHGLMProvider extends GlobalLootModifierProvider {

	public static final CdcVal<ReplaceItemModifier> REPLACE_ITEM =
			CdcReg.of(GensokyoLegacy.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS)
					.reg("replace_item", ReplaceItemModifier.CODEC);

	public static void register() {

	}

	public YHGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, GensokyoLegacy.MODID);
	}

	@Override
	protected void start() {
		add("fishing_lamprey", new ReplaceItemModifier(0.1f, YHFood.RAW_LAMPREY.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING.location()).build()
		));

		add("udumbara_ancient_city_loot", loot(YHLootGen.UDUMBARA_LOOT.location(),
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY.location()).build()));
		add("udumbara_ancient_city_ice_box_loot", loot(YHLootGen.UDUMBARA_LOOT.location(),
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX.location()).build()));

		// 怪肉掉落
		add("scavenging_flesh", create(YHFood.FLESH.item.get(), 1,
				killedByKnife(), fire(false), isFleshSource(), killedByYoukai()));
		add("scavenging_flesh_cooked", create(YHFood.COOKED_FLESH.item.get(), 1,
				killedByKnife(), fire(true), isFleshSource(), killedByYoukai()));

		// 露米娅发带：头颅必掉
		add("rumia_scavenging_skeleton_skull", create(Items.SKELETON_SKULL, 1,
				killedByRumia(), entity(GLTagGen.SKULL_SOURCE)));
		add("rumia_scavenging_wither_skeleton_skull", create(Items.WITHER_SKELETON_SKULL, 1,
				killedByRumia(), entity(GLTagGen.WITHER_SOURCE)));
		add("rumia_scavenging_zombie_head", create(Items.ZOMBIE_HEAD, 1,
				killedByRumia(), entity(GLTagGen.ZOMBIE_SOURCE)));
		add("rumia_scavenging_creeper_head", create(Items.CREEPER_HEAD, 1,
				killedByRumia(), entity(GLTagGen.CREEPER_SOURCE)));
		add("rumia_scavenging_piglin_head", create(Items.PIGLIN_HEAD, 1,
				killedByRumia(), entity(GLTagGen.PIGLIN_SOURCE)));
	}

	private static LootItemCondition killedByRumia() {
		return KilledByRumiaHairbandCondition.INSTANCE;
	}

	private static LootItemCondition entity(TagKey<EntityType<?>> tag) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(tag))).build();
	}

	private static LootItemCondition isFleshSource() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(GLTagGen.FLESH_SOURCE))).build();
	}

	private static LootItemCondition killedByKnife() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().equipment(
						net.minecraft.advancements.critereon.EntityEquipmentPredicate.Builder.equipment().mainhand(
								ItemPredicate.Builder.item().of(TagRef.TOOLS_KNIVES))
								.build()).build()).build();
	}

	private static LootItemCondition killedByYoukai() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().effects(MobEffectsPredicate.Builder.effects().and(YHEffects.YOUKAIFYING))
		).or(LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().effects(MobEffectsPredicate.Builder.effects().and(YHEffects.YOUKAIFIED))
		)).build();
	}

	private static LootItemCondition fire(boolean fire) {
		return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().flags(
						EntityFlagsPredicate.Builder.flags().setOnFire(fire)).build()).build();
	}

	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return AddLootTableModifierAccessor.createAddLootTableModifier(cond, id);
	}

	private static AddItemModifier create(Item item, int count, LootItemCondition... conditions) {
		var ans = AddItemModifierAccessor.create(conditions, item, count);
		assert ans != null;
		return ans;
	}

	private static <T> EntryHolder<T> vanilla(T obj, String id) {
		return new EntryHolder<>(obj, ResourceLocation.withDefaultNamespace(id));
	}

	private record EntryHolder<T>(T type, ResourceLocation id) {

	}

}
