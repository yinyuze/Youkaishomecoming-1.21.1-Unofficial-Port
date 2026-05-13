package dev.xkmc.youkaishomecoming.init.data;

import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2core.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2core.serial.advancements.CriterionBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.content.trigger.FeedCharacterTrigger;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.*;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class GLAdvGen {

    public static final ResourceLocation KOISHI_FIRST = GensokyoLegacy.loc("koishi_first");
    public static final ResourceLocation FLESH_WARN = GensokyoLegacy.loc("flesh_warn");
    public static final ResourceLocation HURT_WARN = GensokyoLegacy.loc("hurt_warn");
    public static final ResourceLocation FEED_REIMU = GensokyoLegacy.loc("main/feed_reimu");

    public static void genAdv(RegistrateAdvancementProvider pvd) {
        pvd.accept(Advancement.Builder.advancement().addCriterion("koishi_first",
                GLCriteriaTriggers.KOISHI_FIRST.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
        ).build(KOISHI_FIRST));
        pvd.accept(Advancement.Builder.advancement().addCriterion("flesh_warn",
                GLCriteriaTriggers.FLESH_WARN.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
        ).build(FLESH_WARN));
        pvd.accept(Advancement.Builder.advancement().addCriterion("hurt_warn",
                GLCriteriaTriggers.HURT_WARN.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
        ).build(HURT_WARN));

        var gen = new AdvancementGenerator(pvd, GensokyoLegacy.MODID);
        var b = gen.new TabBuilder("main");
        var root = b.root("welcome_to_gensokyo", GLItems.REIMU_HAIRBAND.asStack(),
                CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
                "Youkai's Homecoming", "Welcome To Youkai's Homecoming");

        var dango = root.create("sweet", YHFood.MOCHI.item.asStack(),
                CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
                        ItemPredicate.Builder.item().of(YHTagGen.DANGO))),
                "Sweet?", "Eat a Mochi");
        dango.create("breed_deer", YHFood.SAKURA_MOCHI.asItem(),
                CriterionBuilder.one(BredAnimalsTrigger.TriggerInstance.bredAnimals(
                        EntityPredicate.Builder.entity().of(GLEntities.DEER.get()))),
                "Oh Deer", "Breed deer with sakura mochi");
        dango.create("hmm", YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.asStack(),
                CriterionBuilder.item(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get()),
                "Hmm... Is it right?", "Get a Sweet Ormosia Mochi Mixed Boiled");

        root.create("soybean", YHCrops.SOYBEAN.getSeed(),
                CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(
                                BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_SOYBEAN)),
                        ItemPredicate.Builder.item().of(YHCrops.SOYBEAN.getSeed()))),
                "The Essential Harvest", "Plant Soybean");

        root.create("cucumber", YHCrops.CUCUMBER.getSeed(),
                        CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location().setBlock(
                                        BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_SOYBEAN)),
                                ItemPredicate.Builder.item().of(YHCrops.CUCUMBER.getSeed()))),
                        "Rope Climber", "Plant Cucumber")
                .create("cucumber_top", YHCrops.CUCUMBER.getFruits(),
                        CriterionBuilder.one(YHCriteriaTriggers.CUCUMBER.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "Pinnacle Kappa", "Let cucumber climb ropes and harvest the top cucumber of a 3-block tall cucumber crop");

        var grape = root.create("grape", YHCrops.RED_GRAPE.getSeed(),
                CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(
                                BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_SOYBEAN)),
                        ItemPredicate.Builder.item().of(YHTagGen.GRAPE_SEED))),
                "Sweet Vines", "Plant Grape");
        grape.create("grape_cut", Items.SHEARS,
                        CriterionBuilder.one(YHCriteriaTriggers.GRAPE_CUT.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "Niwaki", "Cut the leaves off a mature grape crop so that it can grow larger. Make sure it has 3 ropes in a row to climb onto.")
                .create("grape_harvest", YHCrops.RED_GRAPE.getFruits(),
                        CriterionBuilder.one(YHCriteriaTriggers.GRAPE_HARVEST.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "The Best Bunch", "Harvest grape hanging under a grape branch");
        grape.create("squeeze", YHBlocks.BASIN.asItem(),
                CriterionBuilder.one(YHCriteriaTriggers.BASIN.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                "Squeeze!", "Jump in the basin to squeeze juice out of grapes");

        root.create("redbean", YHCrops.REDBEAN.getSeed(),
                CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(
                                BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_REDBEAN)),
                        ItemPredicate.Builder.item().of(YHCrops.REDBEAN.getSeed()))),
                "Leanness Resistant Red Bean", "Plant Red Bean on Coarse Dirt, Mud, or Clay");

        root.create("coffea", YHCrops.COFFEA.getSeed(),
                        CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location().setBlock(
                                        BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_COFFEA)),
                                ItemPredicate.Builder.item().of(YHCrops.COFFEA.getSeed()))),
                        "Needs Nutrition Coffea", "Plant Coffea on Podzol, Mud, or Soul Soil")
                .create("coffee_era", YHItems.COFFEE_POWDER.asStack(),
                        CriterionBuilder.item(YHItems.COFFEE_POWDER.get()),
                        "Coffee Era", "Get Coffee Powder")
                .create("fragrant", YHCoffee.ESPRESSO.item.asStack(),
                        CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(YHCoffee.ESPRESSO.item.get())),
                        "Fragrant!", "Drink Espresso")
                .create("q_grader", YHCoffee.ESPRESSO.item.asStack(),
                        Util.make(CriterionBuilder.and(), c -> Arrays.stream(YHCoffee.values())
                                .map(e -> e.item.get()).map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
                                .forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
                        "Q Grader", "Drink all kinds of coffee")
                .type(AdvancementType.CHALLENGE, true, true, false);

        var tea = root.create("tea", YHCrops.TEA.getSeed(),
                CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(
                                BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_TEA)),
                        ItemPredicate.Builder.item().of(YHCrops.TEA.getSeed()))),
                "Refreshing Hobby", "Plant Tea on grass block.");
        tea.create("tea_leaves", YHCrops.TEA.getFruits(), CriterionBuilder.item(YHCrops.TEA.getFruits()),
                        "Mellow Fragrance", "Collect tea leaves before tea crop starts flowering")
                .create("tea_master", YHDrink.OOLONG_TEA.item.asStack(),
                        Util.make(CriterionBuilder.and(), c -> Stream.of(
                                        YHDrink.WHITE_TEA, YHDrink.OOLONG_TEA, YHDrink.GREEN_TEA, YHDrink.DARK_TEA, YHDrink.BLACK_TEA, YHDrink.YELLOW_TEA
                                ).map(e -> e.item.get()).map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
                                .forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
                        "Tea Master", "Drink all kinds of tea in original flavor")
                .type(AdvancementType.GOAL, true, true, false);

        root.create("udumbara", YHCrops.UDUMBARA.getWildPlant().asItem(),
                        CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location().setBlock(
                                        BlockPredicate.Builder.block().of(Blocks.FARMLAND)),
                                ItemPredicate.Builder.item().of(YHCrops.UDUMBARA.getSeed()))),
                        "Moon Crop", "Plants an Udumbara with its leaves. It grows under moonlight and shrinks under sunlight.")
                .create("udumbara_leaves", YHCrops.UDUMBARA.getSeed(),
                        CriterionBuilder.one(YHCriteriaTriggers.UDUMBARA_LEAVES.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "Transplant", "Harvest Udumbara Leaves from Udumbara crop near maturity")
                .create("udumbara_flower", YHCrops.UDUMBARA.getFruits(),
                        CriterionBuilder.item(YHCrops.UDUMBARA.getFruits()),
                        "Fragile Flower", "Get an Udumbara flower. It will only appear for 10 seconds during full moon.")
                .type(AdvancementType.CHALLENGE);

        root.create("alcoholic", YHBlocks.FERMENT.asStack(),
                        CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
                                MobEffectsPredicate.Builder.effects().and(YHEffects.DRUNK))),
                        "Alcoholic", "Brew and drink an alcoholic drink and obtain Drunk effect")
                .create("passed_out", YHDrink.DASSAI.item.asStack(),
                        CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
                                MobEffectsPredicate.Builder.effects().and(YHEffects.DRUNK,
                                        new MobEffectsPredicate.MobEffectInstancePredicate(
                                                MinMaxBounds.Ints.atLeast(4), MinMaxBounds.Ints.ANY,
                                                Optional.empty(), Optional.empty())))),
                        "Passed Out", "Drink until you have maximum Drunk effect");

        root.create("crab_grab", YHItems.CRAB_BUCKET.asItem(),
                CriterionBuilder.one(YHCriteriaTriggers.CRAB_GRAB.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                "Crab Grab", "Have a crab grab your water bucket when attempting to bucket a crab");

        root.create("breed_boar", Items.CARROT,
                CriterionBuilder.one(BredAnimalsTrigger.TriggerInstance.bredAnimals(
                        EntityPredicate.Builder.entity().of(GLEntities.BOAR.get()))),
                "Pig with Tusks", "Breed boar with carrot, potato, or beetroot");

        root.create("small_pot", YHBlocks.IRON_POT.asStack(),
                        CriterionBuilder.one(YHCriteriaTriggers.COOKING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "Hotpot", "Cooking with small iron pot / short pot / stockpot")
                .create("pot_grab", YHBowl.POWER_SOUP.asItem(),
                        CriterionBuilder.one(YHCriteriaTriggers.POT_GRAB.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "Shared Enjoyment", "Use small iron pot to take a serve of food from short pot / stockpot");

        var steam = root.create("steamer", YHBlocks.STEAMER_POT.asStack(),
                CriterionBuilder.one(YHCriteriaTriggers.STEAM.get().steam(MinMaxBounds.Ints.ANY)),
                "Steam and Racks", "Steam some food");
        steam.create("dish", YHDish.IMITATION_BEAR_PAW.block.asStack(),
                        CriterionBuilder.one(YHCriteriaTriggers.STEAM.get().steam(YHTagGen.STEAM_BLOCKER)),
                        "Steam Interceptor", "Steam a plate meal or bamboo meal. Note that they heavily obstruct steam.")
                .type(AdvancementType.GOAL);
        steam.create("max_steamer", YHBlocks.STEAMER_RACK.asStack(),
                        CriterionBuilder.one(YHCriteriaTriggers.STEAM.get().steam(MinMaxBounds.Ints.atLeast(18))),
                        "Bamboo Tower", "Steam an item on the 18th rack")
                .type(AdvancementType.CHALLENGE);

        var table = root.create("cuisine_board", YHBlocks.CUISINE_BOARD.asStack(),
                CriterionBuilder.one(YHCriteriaTriggers.TABLE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                "Sushi Apprentice", "Make something with cuisine board");
        // kaguya_hime 成就暂时跳过（YHBowl.KAGUYA_HIME 当前被注释禁用）
        // table.create("kaguya_hime", ...)
        table.create("sushi_master", YHSushi.OTORO_NIGIRI.asItem(),
                        CriterionBuilder.and()
                                .add(InventoryChangeTrigger.TriggerInstance.hasItems(YHSushi.OTORO_NIGIRI.asItem()))
                                .add(InventoryChangeTrigger.TriggerInstance.hasItems(YHSushi.TOBIKO_GUNKAN.asItem()))
                                .add(InventoryChangeTrigger.TriggerInstance.hasItems(YHSushi.LORELEI_NIGIRI.asItem()))
                                .add(InventoryChangeTrigger.TriggerInstance.hasItems(YHRolls.RAINBOW_FUTOMAKI.slice.asItem())),
                        "Sushi Master", "Make Otoro Nigiri, Lorelei Nigiri, Tobiko Gunkan, and Rainbow Futomaki Slice")
                .type(AdvancementType.GOAL);
        table.create("foreign_sushi_master", YHRolls.RAINBOW_ROLL.slice.asItem(),
                        CriterionBuilder.and()
                                .add(InventoryChangeTrigger.TriggerInstance.hasItems(YHRolls.RAINBOW_ROLL.slice.asItem()))
                                .add(InventoryChangeTrigger.TriggerInstance.hasItems(YHRolls.VOLCANO_ROLL.slice.asItem())),
                        "Sushi Aboard", "Make Rainbow Roll Slice and Volcano Roll Slice")
                .type(AdvancementType.GOAL);

        var youkai = root.create("flesh", YHFood.FLESH.item.asStack(),
                        CriterionBuilder.item(YHFood.FLESH.item.get()),
                        "Where is it from?", "Get weird meat")
                .type(AdvancementType.GOAL, true, true, false)
                .create("first_time", YHFood.FLESH.item.asStack(),
                        CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
                                MobEffectsPredicate.Builder.effects().and(YHEffects.YOUKAIFYING))),
                        "The First Time", "Get Youkaifying effect")
                .type(AdvancementType.CHALLENGE, true, true, true);

        youkai.create("bloody", YHItems.BLOOD_BOTTLE.asStack(1),
                        CriterionBuilder.item(YHItems.BLOOD_BOTTLE.item.get()),
                        "Bloody!", "Get a Blood Bottle")
                .create("monstrosity", YHFood.BOWL_OF_FLESH_FEAST.item.asStack(),
                        CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(YHFood.BOWL_OF_FLESH_FEAST.item.get())),
                        "Monstrosity", "Eat a bowl of flesh feast")
                .type(AdvancementType.GOAL, true, true, false);

        var youkaified = youkai.create("powerful_being", GLItems.SUWAKO_HAT.asStack(),
                        CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
                                MobEffectsPredicate.Builder.effects().and(YHEffects.YOUKAIFIED))),
                        "Powerful Being", "Get Youkaified effect")
                .type(AdvancementType.CHALLENGE, true, true, false);

        youkai.create("mary_call", Items.IRON_SWORD,
                        CriterionBuilder.one(GLCriteriaTriggers.KOISHI_RING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "It's Mary-san", "Receives a phone call from Koishi in the nether in Youkaifying effect")
                .create("koishi_hat", GLItems.KOISHI_HAT.get(),
                        CriterionBuilder.item(GLItems.KOISHI_HAT.get()),
                        "Koishi's Hat", "Obtain Koishi's Hat after playing with Koishi")
                .type(AdvancementType.CHALLENGE, true, true, false);

        youkai.create("suwako_wear", GLItems.STRAW_HAT.get(),
                        CriterionBuilder.one(GLCriteriaTriggers.SUWAKO_WEAR.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                        "Godhood Ascension", "In youkaified or youkaifying effect, give the straw hat to a frog")
                .create("suwako_hat", GLItems.SUWAKO_HAT.get(),
                        CriterionBuilder.item(GLItems.SUWAKO_HAT.get()),
                        "Faith Collection", "Obtain Suwako's Hat after collecting enough faith")
                .type(AdvancementType.CHALLENGE, true, true, false);

        var danmaku = youkaified.create("trade_danmaku", YHFood.FLESH_CHOCOLATE_MOUSSE.item.get(),
                CriterionBuilder.one(GLCriteriaTriggers.TRADE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
                "Cute Merchant", "In youkaified effect, trade with Rumia to get Danmaku");

        danmaku.create("lost_memories", GLItems.RUMIA_HAIRBAND.get(),
                        CriterionBuilder.item(GLItems.RUMIA_HAIRBAND.get()),
                        "Lost Memories", "When Rumia takes more than 40 damage, she will convert to Ex. Rumia. Defeat Ex. Rumia with danmaku and obtain Rumia's Hairband.")
                .type(AdvancementType.CHALLENGE);

        danmaku.create("spellcard_power", GLItems.REIMU_SPELL.get(),
                        CriterionBuilder.item(GLItems.REIMU_SPELL.get()),
                        "Spellcard Power", "When you eat flesh in front of villagers, Reimu will try to exterminate you. Defeat Reimu and obtain her spellcard")
                .type(AdvancementType.CHALLENGE);

        danmaku.create("feed_reimu", GLItems.REIMU_HAIRBAND.get(),
                Util.make(CriterionBuilder.and(), c -> Streams.concat(
                                Arrays.stream(YHDish.values()).map(e -> e.block.get()),
                                Arrays.stream(YHDrink.values()).filter(e -> !e.isFlesh()).map(e -> e.item.get()),
                                Arrays.stream(YHFood.values()).filter(e -> e.type.isReimuFood()).map(e -> e.item.get()),
                                Arrays.stream(YHBowl.values()).filter(YHBowl::isReimuFood).map(e -> e.item.get()),
                                Arrays.stream(YHSushi.values()).filter(YHSushi::isReimuFood).map(e -> e.item.get()),
                                Arrays.stream(YHRolls.values()).map(e -> e.slice.get()))
                        .distinct()
                        .map(e -> Pair.of(e, FeedCharacterTrigger.TriggerInstance.usedItem(null, null,
                                ItemPredicate.Builder.item().of(e).build())))
                        .forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
                "Satisfied Reimu", "Feed Reimu all appealing food from Youkai's Homecoming to make her happy and give you her hairband");

        root.create("mousse", YHFood.KOISHI_MOUSSE.item.asStack(),
                        CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
                                ItemPredicate.Builder.item().of(YHFood.KOISHI_MOUSSE.item.get()))),
                        "Well... Yes? What's Just Happened?", "Eat a Koishi Mousse")
                .type(AdvancementType.GOAL, true, true, true);

        // "enthusiastic" advancement is now managed by hand-written JSON under
        // src/main/resources/data/youkaishomecoming/advancement/; code-side
        // generation removed to avoid duplicate-criterion conflicts (YHFood
        // proxies some YHDrink items, producing same-key criteria).

        root.finish();
    }

}
