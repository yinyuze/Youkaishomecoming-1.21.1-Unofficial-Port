package dev.xkmc.youkaishomecoming.compat.terrablender;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import terrablender.api.Regions;

public class TerraBlenderCompat {

    public static void registerBiomes() {
        Regions.register(new GensokyoRegion(GensokyoLegacy.loc("overworld"), 2));
    }
}
