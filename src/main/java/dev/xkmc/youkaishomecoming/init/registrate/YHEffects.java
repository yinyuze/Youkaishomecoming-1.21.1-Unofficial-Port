package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.LegacyHolder;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.effect.*;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class YHEffects {

	public static final LegacyHolder<MobEffect> CAFFEINATED = genEffect("caffeinated",
			() -> new CaffeinatedEffect(MobEffectCategory.BENEFICIAL, -10667225),
			"Boost attack damage");

	public static final LegacyHolder<MobEffect> TEA = genEffect("tea_polyphenols",
			() -> new TeaEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Boost attack speed, heal every 3 seconds when under sunlight");

	public static final LegacyHolder<MobEffect> SOBER = genEffect("sober",
			() -> new SoberEffect(MobEffectCategory.NEUTRAL, 0x21c189),
			"Prevents phantom spawn, nausea, youkaization, and sleep. " +
					"You can't have another alcohol or sobering drink while having this effect.");

	public static final LegacyHolder<MobEffect> DRUNK = genEffect("drunk",
			() -> new DrunkEffect(MobEffectCategory.NEUTRAL, 0xF9E6CD),
			"Increase attack damage, but you feel drunk. " +
					"You can't have sobering drink while having this effect. " +
					"Drunk effect can stack up to 5 layers.");

	public static final LegacyHolder<MobEffect> REFRESHING = genEffect("refreshing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xd0c3a5),
			"Immune to fire, puts down fire");

	public static final LegacyHolder<MobEffect> THICK = genEffect("thick",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x534b40),
			"Immune to wither, reduces damage by 1");

	public static final LegacyHolder<MobEffect> SMOOTHING = genEffect("smoothing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Immune to poison, improves health regeneration");

	public static final LegacyHolder<MobEffect> APHRODISIAC = genEffect("aphrodisiac",
			() -> new MandrakeEffect(MobEffectCategory.NEUTRAL, 0xE17A84),
			"Animals will enter breeding state");

	public static final LegacyHolder<MobEffect> HYPNOSIS = genEffect("hypnosis",
			() -> new EmptyEffect(MobEffectCategory.NEUTRAL, 0xC6B3DF),
			"Villagers will ignore your crimes");

	public static final LegacyHolder<MobEffect> UDUMBARA = genEffect("phantom",
			() -> new UdumbaraEffect(MobEffectCategory.BENEFICIAL, 0xFBDCFD),
			"Prevents vibration from you and your projectiles. " +
					"Warps you to the top of the world when falling into the void. " +
					"Heals every 3 seconds under moonlight. " +
					"Reduced damage taken when under full moon.");

	public static final LegacyHolder<MobEffect> YOUKAIFIED = genEffect("youkaified",
			() -> new YoukaifiedEffect(MobEffectCategory.NEUTRAL, 0x8B4513),
			"You are youkaified. Increases max health, attack damage, and movement speed, but you need more food.");

	public static final LegacyHolder<MobEffect> YOUKAIFYING = genEffect("youkaifying",
			() -> new YoukaifyingEffect(MobEffectCategory.NEUTRAL, 0xC86432),
			"You are being youkaified. Consuming more flesh food accelerates the process.");

	public static final LegacyHolder<MobEffect> FAIRY = genEffect("fairy",
			() -> new FairyEffect(MobEffectCategory.NEUTRAL, 0x88CCFF),
			"You are fairy-fied. Reduces max health and attack damage, but increases movement speed.");

	public static final LegacyHolder<MobEffect> HIGI = genEffect("higi",
			() -> new HigiEffect(MobEffectCategory.BENEFICIAL, 0xFFD700),
			"Increases attack damage and movement speed, and regenerates health periodically.");

	public static final LegacyHolder<MobEffect> EARTHY = genEffect("earthy",
			() -> new EarthyEffect(MobEffectCategory.BENEFICIAL, 0x8B6914),
			"Regenerates health when well-fed.");

	public static final LegacyHolder<MobEffect> CRABY = genEffect("craby",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Makes your horizontal movement faster");

	public static final LegacyHolder<MobEffect> MEDITATION = genEffect("meditation",
			() -> new MeditationEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Gain experience while standing still");

	public static final LegacyHolder<MobEffect> BREATHING = genEffect("breathing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Extends underwater breathing time");

	public static final LegacyHolder<MobEffect> CONFIDENT = genEffect("confident",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Boost confidence");

	public static final LegacyHolder<MobEffect> ENJOYABLE = genEffect("enjoyable",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Enjoyable");

	public static final LegacyHolder<MobEffect> MATURE = genEffect("mature",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Mature");

	public static final LegacyHolder<MobEffect> FLAMING = genEffect("flaming",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Flaming");

	private static <T extends MobEffect> LegacyHolder<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(GensokyoLegacy.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	private static final List<Consumer<PotionBrewing.Builder>> TEMP = new ArrayList<>();

	public static void registerBrewingRecipe(RegisterBrewingRecipesEvent event) {
		var builder = event.getBuilder();
		TEMP.forEach(e -> e.accept(builder));
	}

	private static <T extends Potion> SimpleEntry<Potion> genPotion(String name, NonNullSupplier<T> sup) {
		SimpleEntry<Potion> ans = GensokyoLegacy.REGISTRATE.potion(name, sup);
		return ans;
	}

	private static void regPotion2(String id, Holder<MobEffect> sup, Supplier<Item> item, int dur, int durLong, Holder<Potion> from) {
		SimpleEntry<Potion> potion = genPotion(id, () -> new Potion(new MobEffectInstance(sup, dur)));
		SimpleEntry<Potion> longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance(sup, durLong)));
		TEMP.add((e) -> {
			e.addMix(from, item.get(), potion.holder());
			e.addMix(potion.holder(), Items.REDSTONE, longPotion.holder());
		});
	}

	public static void register() {
		regPotion2("aphrodisiac", APHRODISIAC, YHItems.DRIED_MANDRAKE_FLOWER, 3600, 9600, Potions.HEALING);
		regPotion2("hypnosis", HYPNOSIS, YHItems.STRIPPED_MANDRAKE_ROOT, 3600, 9600, Potions.WEAKNESS);
	}

}
