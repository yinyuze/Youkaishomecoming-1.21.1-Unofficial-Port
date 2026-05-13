package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.youkaishomecoming.content.attachment.datamap.BedData;
import dev.xkmc.youkaishomecoming.init.registrate.GLBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.GLEntities;
import dev.xkmc.youkaishomecoming.init.registrate.GLMeta;
import dev.xkmc.youkaishomecoming.init.data.structure.GLStructureGen;

public class GLDataMapGen {

    public static void dataMapGen(RegistrateDataMapProvider pvd) {
        GLStructureGen.dataMap(pvd);
        // register donation_box as a reimu bed so DonationBoxBlockEntity.take() can find the entity type
        pvd.builder(GLMeta.BED_DATA.reg())
                .add(GLBlocks.DONATION_BOX, new BedData(GLEntities.REIMU.get()), false);
    }

}
