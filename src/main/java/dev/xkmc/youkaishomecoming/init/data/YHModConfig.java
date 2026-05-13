package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.neoforged.neoforge.common.ModConfigSpec;

public class YHModConfig {

	public static class Server extends ConfigInit {

		public final ModConfigSpec.DoubleValue smoothingHealingFactor;
		public final ModConfigSpec.IntValue teaHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraDuration;
		public final ModConfigSpec.IntValue udumbaraHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraFullMoonReduction;
		public final ModConfigSpec.IntValue youkaifyingTime;
		public final ModConfigSpec.IntValue youkaifyingThreshold;
		public final ModConfigSpec.IntValue youkaifiedDuration;
		public final ModConfigSpec.IntValue youkaifyingChance;
		public final ModConfigSpec.IntValue youkaifyingConfusionTime;
		public final ModConfigSpec.IntValue youkaifiedProlongation;

		Server(Builder builder) {
			markPlain();
			smoothingHealingFactor = builder.text("Smoothing Healing Factor")
					.defineInRange("smoothingHealingFactor", 1.5, 1, 100);
			teaHealingPeriod = builder.text("Tea Healing Interval")
					.defineInRange("teaHealingPeriod", 60, 0, 10000);
			udumbaraHealingPeriod = builder.text("Udumbara effect Healing Interval")
					.defineInRange("udumbaraHealingPeriod", 60, 0, 10000);
			udumbaraDuration = builder.text("Udumbara flowering duration")
					.defineInRange("udumbaraDuration", 200, 0, 100000);
			udumbaraFullMoonReduction = builder.text("Udumbara full moon damage reduction")
					.defineInRange("udumbaraFullMoonReduction", 4, 0, 100);
			youkaifyingTime = builder.text("Ticks of Youkaifying effect given by flesh food (1 min = 1200)")
					.defineInRange("youkaifyingTime", 1200, 0, 100000);
			youkaifyingThreshold = builder.text("Tick threshold of accumulated Youkaifying time before converting to Youkaified (5 min = 6000)")
					.defineInRange("youkaifyingThreshold", 6000, 0, 1000000);
			youkaifiedDuration = builder.text("Duration of Youkaified effect in ticks (20 min = 24000)")
					.defineInRange("youkaifiedDuration", 24000, 0, 1000000);
			youkaifyingChance = builder.text("Chance (out of 100) that flesh food triggers Youkaifying for the first time")
					.defineInRange("youkaifyingChance", 20, 0, 100);
			youkaifyingConfusionTime = builder.text("Duration of Confusion from youkaification in ticks")
					.defineInRange("youkaifyingConfusionTime", 200, 0, 100000);
			youkaifiedProlongation = builder.text("Ticks added to Youkaified effect when eating flesh while already youkaified (5 min = 6000)")
					.defineInRange("youkaifiedProlongation", 6000, 0, 100000);

		}

	}

	public static final Server SERVER = GensokyoLegacy.REGISTRATE.registerSynced(Server::new);

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
	}


}
