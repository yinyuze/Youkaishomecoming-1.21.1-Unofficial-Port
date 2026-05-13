package dev.xkmc.youkaishomecoming.init.data.loot;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EntityLootGen {

	public static <T extends LivingEntity> void noLoot(RegistrateEntityLootTables pvd, EntityType<T> type) {
		pvd.add(type, LootTable.lootTable());
	}


	public static void reimu(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(GLItems.REIMU_SPELL.get(), 1, 1))
						.when(byPlayer()).when(danmakuKill()))
		);
	}

	public static void sanae(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(GLItems.SANAE_SPELL.get(), 1, 1))
						.when(byPlayer()).when(danmakuKill()))
		);
	}

	public static void marisa(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(GLItems.MARISA_SPELL.get(), 1, 1))
						.when(byPlayer()).when(danmakuKill()))
		);
	}

	public static void mystia(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(GLItems.MYSTIA_SPELL.get(), 1, 1))
						.when(byPlayer()).when(danmakuKill()))
		);
	}

	public static void koishi(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(GLItems.KOISHI_SPELL.get(), 1, 1))
						.when(byPlayer()).when(danmakuKill()))
		);
	}

	public static void rumia(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get(), 5, 10))
						.apply(lootCount(pvd, 1f))
						.when(byPlayer()))
				.withPool(LootPool.lootPool().add(getItem(DanmakuItems.Bullet.CIRCLE.get(DyeColor.BLACK).get(), 3, 6))
						.apply(lootCount(pvd, 1f))
						.when(byPlayer()))
		);
	}

	public static void cirno(RegistrateEntityLootTables pvd, EntityType<?> type) {
		var helper = new LootHelper(pvd);
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(helper.item(GLItems.FAIRY_ICE_CRYSTAL.get(), 1, 2))
						.apply(helper.lootCount(0.5f))
						.when(byPlayer()))
				.withPool(LootPool.lootPool()
						.add(helper.item(DanmakuItems.Bullet.MENTOS.get(DyeColor.LIGHT_BLUE).get(), 1, 1)
								.setWeight(6))
						.add(LootItem.lootTableItem(GLItems.FROZEN_FROG_COLD.get()))
						.add(LootItem.lootTableItem(GLItems.FROZEN_FROG_WARM.get()))
						.add(LootItem.lootTableItem(GLItems.FROZEN_FROG_TEMPERATE.get()))
						.when(lootChance(pvd, 0.3f))
						.when(danmakuKill())
						.when(byPlayer()))
		);
	}

	public static void fairy(RegistrateEntityLootTables pvd, EntityType<?> type) {
		var helper = new LootHelper(pvd);
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(helper.item(YHFood.FAIRY_CANDY.item.get(), 1, 2))
						.apply(helper.lootCount(0.5f))
						.when(byPlayer()))
		);
	}

	public static void yukari(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.when(byPlayer()).when(danmakuKill())
						.add(getItem(GLItems.YUKARI_SPELL_LASER.get(), 1, 1))
						.add(getItem(GLItems.YUKARI_SPELL_BUTTERFLY.get(), 1, 1)))
		);
	}

	private static LootItemFunction.Builder onFire() {
		return SmeltItemFunction.smelted()
				.when(LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity()
								.flags(EntityFlagsPredicate.Builder.flags()
										.setOnFire(true))));
	}


	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max) {
		return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)));
	}

	private static LootItemCondition.Builder byPlayer() {
		return LootItemKilledByPlayerCondition.killedByPlayer();
	}

	private static LootItemCondition.Builder danmakuKill() {
		return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
				.tag(TagPredicate.is(DanmakuDamageTypes.DANMAKU_TYPE)));
	}

	private static LootItemFunction.Builder lootCount(RegistrateEntityLootTables pvd, float factor) {
		return EnchantedCountIncreaseFunction.lootingMultiplier(pvd.getRegistries(), UniformGenerator.between(factor * 0.5f, factor));
	}

	private static LootItemCondition.Builder lootChance(RegistrateEntityLootTables pvd, float base) {
		return LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(pvd.getRegistries(), base, base * 0.2f);
	}

	public static void boar(RegistrateEntityLootTables pvd, EntityType<BoarEntity> type) {
		var helper = new LootHelper(pvd);
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(helper.item(YHFood.RAW_BOARCHOP.item.get(), 1, 2))
						.apply(helper.lootCount(0.5f))
						.apply(onFire())));
	}

	public static void deer(RegistrateEntityLootTables pvd, EntityType<DeerEntity> type) {
		var helper = new LootHelper(pvd);
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(helper.item(YHFood.RAW_VENISON.item.get(), 1, 2))
						.apply(helper.lootCount(0.5f))
						.apply(onFire())));
	}

	public static void crab(RegistrateEntityLootTables pvd, EntityType<CrabEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.CRAB.item.get())))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.CRAB_ROE.item.get()))
						.when(LootItemRandomChanceCondition.randomChance(0.5f))));
	}

	public static void tuna(RegistrateEntityLootTables pvd, EntityType<TunaEntity> type) {
		var helper = new LootHelper(pvd);
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.RAW_TUNA.item.get()))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))
						.apply(helper.lootCount(0.5f))
						.apply(onFire()))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.BONE_MEAL))
						.when(LootItemRandomChanceCondition.randomChance(0.05F)))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.OTORO.item.get()))
						.apply(helper.lootCount(0.5f))));
	}

	public static void lamprey(RegistrateEntityLootTables pvd, EntityType<?> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.RAW_LAMPREY.item.get()))));
	}

}
