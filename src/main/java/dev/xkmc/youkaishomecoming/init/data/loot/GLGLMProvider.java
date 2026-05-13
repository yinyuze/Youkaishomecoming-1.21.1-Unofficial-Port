package dev.xkmc.youkaishomecoming.init.data.loot;

import dev.xkmc.l2core.serial.loot.AddItemModifier;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLTagGen;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.loot.KilledByRumiaHairbandCondition;
import dev.xkmc.youkaishomecoming.init.loot.ReplaceItemModifier;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.GLEntities;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import dev.xkmc.youkaishomecoming.mixin.AddLootTableModifierAccessor;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class GLGLMProvider extends GlobalLootModifierProvider {

    public static void register() {

    }

    public GLGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, GensokyoLegacy.MODID);
    }

    @Override
    protected void start() {
        // 琪露诺冰冻青蛙
        add("cirno_frozen_frog_cold", createL2(GLItems.FROZEN_FROG_COLD.get(),
                killedByCirno(), frog(FrogVariant.COLD)));
        add("cirno_frozen_frog_warm", createL2(GLItems.FROZEN_FROG_WARM.get(),
                killedByCirno(), frog(FrogVariant.WARM)));
        add("cirno_frozen_frog_temperate", createL2(GLItems.FROZEN_FROG_TEMPERATE.get(),
                killedByCirno(), frog(FrogVariant.TEMPERATE)));

        // 露米娅发带：头颅必掉
        add("rumia_scavenging_skeleton_skull", createL2(Items.SKELETON_SKULL,
                KilledByRumiaHairbandCondition.INSTANCE, entity(GLTagGen.SKULL_SOURCE)));
        add("rumia_scavenging_wither_skeleton_skull", createL2(Items.WITHER_SKELETON_SKULL,
                KilledByRumiaHairbandCondition.INSTANCE, entity(GLTagGen.WITHER_SOURCE)));
        add("rumia_scavenging_zombie_head", createL2(Items.ZOMBIE_HEAD,
                KilledByRumiaHairbandCondition.INSTANCE, entity(GLTagGen.ZOMBIE_SOURCE)));
        add("rumia_scavenging_creeper_head", createL2(Items.CREEPER_HEAD,
                KilledByRumiaHairbandCondition.INSTANCE, entity(GLTagGen.CREEPER_SOURCE)));
        add("rumia_scavenging_piglin_head", createL2(Items.PIGLIN_HEAD,
                KilledByRumiaHairbandCondition.INSTANCE, entity(GLTagGen.PIGLIN_SOURCE)));

        // 钓鱼
        add("fishing_lamprey", new ReplaceItemModifier(0.1f, YHFood.RAW_LAMPREY.item.asStack(),
                LootTableIdCondition.builder(BuiltInLootTables.FISHING.location()).build()));

        // 遗迹宝箱
        add("udumbara_ancient_city_loot", loot(YHLootGen.UDUMBARA_LOOT.location(),
                LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY.location()).build()));
        add("udumbara_ancient_city_ice_box_loot", loot(YHLootGen.UDUMBARA_LOOT.location(),
                LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX.location()).build()));

        // 怪肉掉落
        add("scavenging_flesh", createFD(YHFood.FLESH.item.get(), 1,
                killedByKnife(), fire(false), isFleshSource(), killedByYoukai()));
        add("scavenging_flesh_cooked", createFD(YHFood.COOKED_FLESH.item.get(), 1,
                killedByKnife(), fire(true), isFleshSource(), killedByYoukai()));

        // 露米娅发带：不需要小刀也掉怪肉
        add("rumia_scavenging_flesh", createFD(YHFood.FLESH.item.get(), 1,
                KilledByRumiaHairbandCondition.INSTANCE, fire(false), isFleshSource()));
        add("rumia_scavenging_flesh_cooked", createFD(YHFood.COOKED_FLESH.item.get(), 1,
                KilledByRumiaHairbandCondition.INSTANCE, fire(true), isFleshSource()));
    }

    // --- condition helpers ---

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
                        EntityEquipmentPredicate.Builder.equipment().mainhand(
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

    private static LootItemCondition killedByCirno() {
        return LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.ATTACKER,
                        EntityPredicate.Builder.entity().entityType(
                                EntityTypePredicate.of(GLEntities.CIRNO.get())))
                .or(LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.ATTACKER,
                        EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment()
                                        .head(ItemPredicate.Builder.item().of(GLItems.CIRNO_HAIRBAND.get())).build()
                        ).build()))
                .build();
    }

    private static LootItemCondition entity(TagKey<EntityType<?>> tag) {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().entityType(
                        EntityTypePredicate.of(tag))).build();
    }

    private LootItemCondition frog(ResourceKey<FrogVariant> type) {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().of(EntityType.FROG)
                        .subPredicate(EntitySubPredicates.FROG.createPredicate(
                                HolderSet.direct(registries.holderOrThrow(type))))
        ).build();
    }

    private static LootItemCondition fire(boolean fire) {
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().flags(
                        EntityFlagsPredicate.Builder.flags().setOnFire(fire)).build()).build();
    }

    // --- modifier factories ---

    private static dev.xkmc.l2core.serial.loot.AddItemModifier createL2(Item item, LootItemCondition... conditions) {
        return new dev.xkmc.l2core.serial.loot.AddItemModifier(item, null, conditions);
    }

    private static vectorwing.farmersdelight.common.loot.modifier.AddItemModifier createFD(Item item, int count, LootItemCondition... conditions) {
        var ans = AddItemModifierAccessor.create(conditions, item, count);
        assert ans != null;
        return ans;
    }

    private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
        return AddLootTableModifierAccessor.createAddLootTableModifier(cond, id);
    }

}
