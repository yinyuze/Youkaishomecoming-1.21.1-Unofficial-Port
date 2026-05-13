package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum YHLangData {
	CHANCE_EFFECT("tooltip.chance", "%1$s with %2$s%% chance", 2, ChatFormatting.GRAY),
	PLACE("tooltip.place", "Can be placed on ground", 0, ChatFormatting.GRAY),
	KETTLE_INFO("tooltip.kettle", "Right click with water bucket or water bottle to fill water", 0, ChatFormatting.GRAY),
	DRYING_RACK("tooltip.drying_rack", "Only works directly under the sun", 0, ChatFormatting.GRAY),

	MOON_LANTERN_PLACE("tooltip.moon_lantern_place", "Udumbara within 3x3x3 blocks below this latern will grow as night as if they can see full moon.", 0, ChatFormatting.GRAY),
	MOON_LANTERN_HOLD("tooltip.moon_lantern_hold", "When holding in hand, Udumbara effect will always trigger at night regardless if player can see full moons", 0, ChatFormatting.GRAY),

	FERMENT_PROGRESS("fermenting_progress", "Fermenting: %s", 1, ChatFormatting.GRAY),
	COOKING_PROGRESS("cooking_progress", "Cooking: %s", 1, ChatFormatting.GRAY),

	FLASK_OF("flask.of", "Flask Of %s", 1, null),
	FLASK_CONTENT("tooltip.flask_content", "Content: %s", 1, ChatFormatting.GRAY),
	FLASK_USE("tooltip.flask_use", "Use: %s/%s", 2, ChatFormatting.GRAY),
	FLASK_INFO_DRINK("tooltip.flask_info_drink", "Can be drunk", 0, ChatFormatting.GRAY),
	FLASK_INFO_SAUCE("tooltip.flask_info_sauce", "Can be used in cooking", 0, ChatFormatting.GRAY),

	CUISINE_ALLOW("cuisine_allow", "Next Step:", 0, ChatFormatting.GRAY),
	CUISINE_EXTRA("cuisine_extra", "And %s more...", 1, ChatFormatting.GRAY),

	JEI_MOKA("jei.moka", "Coffee Brewing", 0, null),
	JEI_KETTLE("jei.kettle", "Tea Brewing", 0, null),
	JEI_RACK("jei.rack", "Drying", 0, null),
	JEI_FERMENT("jei.ferment", "Fermenting", 0, null),
	JEI_CUISINE("jei.cuisine", "Cuisine", 0, null),
	JEI_COOKING("jei.cooking", "Pot Cooking", 0, null),
	JEI_STEAM("jei.steam", "Steaming", 0, null),

	STEAMER_NO_HEAT("tooltip.steamer_no_heat", "No heat source below", 0, ChatFormatting.RED),
	STEAMER_NO_WATER("tooltip.steamer_no_water", "No water in pot", 0, ChatFormatting.RED),
	STEAMER_TOO_MANY("tooltip.steamer_too_many", "Too many racks stacked", 0, ChatFormatting.RED),
	STEAMER_NO_RACK("tooltip.steamer_no_rack", "No rack placed", 0, ChatFormatting.RED),
	STEAMER_NO_CAP("tooltip.steamer_no_cap", "No lid on top", 0, ChatFormatting.RED),

	OBTAIN("obtain", "Source: ", 0, ChatFormatting.GRAY),
	UNKNOWN("unknown", "???", 0, ChatFormatting.GRAY),
	OBTAIN_FLESH("obtain_flesh", "Kill human mobs with knife while in %s or %s effect", 2, ChatFormatting.GRAY),
	OBTAIN_BLOOD("obtain_blood", "Kill human mobs with knife and have glass bottle in off hand while in %s or %s effect", 2, ChatFormatting.GRAY),
	;

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	YHLangData(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = GensokyoLegacy.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public String key() {
		return key;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (YHLangData lang : YHLangData.values()) {
			pvd.add(lang.key, lang.def);
		}
	}


}
