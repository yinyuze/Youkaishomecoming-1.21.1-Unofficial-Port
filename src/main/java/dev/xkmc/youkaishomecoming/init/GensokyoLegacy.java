package dev.xkmc.youkaishomecoming.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMCompat;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.content.attachment.character.CharDataToClient;
import dev.xkmc.youkaishomecoming.content.attachment.misc.FrogSyncPacket;
import dev.xkmc.youkaishomecoming.content.attachment.misc.KoishiStartPacket;
import dev.xkmc.youkaishomecoming.content.client.debug.BlockInfoToClient;
import dev.xkmc.youkaishomecoming.content.client.debug.BlockRequestToServer;
import dev.xkmc.youkaishomecoming.content.client.debug.CharacterInfoToClient;
import dev.xkmc.youkaishomecoming.content.client.debug.CharacterRequestToServer;
import dev.xkmc.youkaishomecoming.content.client.structure.*;
import dev.xkmc.youkaishomecoming.content.entity.behavior.move.PathDataToClient;
import dev.xkmc.youkaishomecoming.content.entity.foundation.CombatToClient;
import dev.xkmc.youkaishomecoming.content.item.character.TouhouMat;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluidWrapper;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleIngredient;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipFluidWrapper;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler;
import dev.xkmc.youkaishomecoming.event.GLAttackListener;
import dev.xkmc.youkaishomecoming.init.data.*;
import dev.xkmc.youkaishomecoming.init.data.loot.GLGLMProvider;
import dev.xkmc.youkaishomecoming.init.data.structure.GLStructureGen;
import dev.xkmc.youkaishomecoming.init.data.structure.GLStructureLootGen;
import dev.xkmc.youkaishomecoming.init.data.structure.GLStructureTagGen;
import dev.xkmc.youkaishomecoming.init.data.structure.ReportBlocksInStructure;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import dev.xkmc.youkaishomecoming.init.registrate.*;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.core.registries.Registries;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.init.reg.simple.IngReg;
import dev.xkmc.l2core.init.reg.simple.IngVal;
import dev.xkmc.l2core.serial.config.ConfigTypeEntry;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GensokyoLegacy.MODID)
@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GensokyoLegacy {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "youkaishomecoming";
    public static final Reg REG = new Reg(MODID);
    public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
    public static SimpleEntry<CreativeModeTab> TAB;
    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(MODID, 1,
            h -> h.create(FrogSyncPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(CharDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(KoishiStartPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(BlockInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(CharacterInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(CombatToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(PathDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(StructureBoundUpdateToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(CustomStructureBoundUpdateToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(StructureInfoUpdateToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
            h -> h.create(BlockRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
            h -> h.create(CharacterRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
            h -> h.create(StructureInfoRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
            h -> h.create(StructureEditToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
            h -> h.create(StructureRepairToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER)
    );

    public static final ConfigTypeEntry<YHFluidHandler.Config> FLUID_MAP = new ConfigTypeEntry<>(HANDLER, "fluid_mapping", YHFluidHandler.Config.class);

    public static final IngVal<SlipBottleIngredient> ING_BOTTLE = IngReg.of(REG).reg("slip_bottle", SlipBottleIngredient.class);

    public GensokyoLegacy() {
        if (TAB == null) {
            TAB = REGISTRATE.buildModCreativeTab("youkais_homecoming", "Youkai's Homecoming",
                    e -> e.icon(YHItems.OOLONG_TEA_BAG::asStack));
        }
        YHBlocks.register();
        GLDecoBlocks.register();
        GLItems.register();
        GLEntities.register();
        YHEntities.register();

        YHItems.register();
        YHEffects.register();
        // YHSake removed: items migrated to YHDrink to avoid duplicate registration

        GLRecipes.register();
        TouhouMat.register();
        GLMeta.register();
        GLMisc.register();
        GLWorldGen.register();
        GLBrains.register();
        GLAttributes.register();
        GLEffects.register();
        GLSounds.register();
        YHSounds.register();
        GLCriteriaTriggers.register();
        dev.xkmc.youkaishomecoming.init.loot.YHGLMProvider.register();
        YHCriteriaTriggers.register();
        GLModConfig.init();
        YHModConfig.init();
        TouhouSpellCards.registerSpells();
        AttackEventHandler.register(1765, new GLAttackListener());
        if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
            NeoForge.EVENT_BUS.register(TLMCompat.class);
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register YHDrink items (unified enum for all drinks including sake)
        for (var e : YHDrink.values()) {
            event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SakeFluidWrapper(stack), e.item().get());
            // Also register large bottles if available
            if (e.set != null) {
                event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SlipFluidWrapper(stack), e.set.bottle.get());
            }
        }

        // Register condiments - removed soy sauce and mayonnaise as they should be used directly as items
        // Only register large flasks which need fluid conversion

        // Register large flasks
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SlipFluidWrapper(stack), YHItems.SOY_SAUCE_FLASK.get());
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SlipFluidWrapper(stack), YHItems.MAYONNAISE_FLASK.get());
        // Register blood bottle as fluid source for fermentation tank
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SakeFluidWrapper(stack), YHItems.BLOOD_BOTTLE.item.get());

        // Register block entity capabilities (from 1.21 source)
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.FERMENT_BE.get(),
            dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.FERMENT_BE.get(),
            dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.BASIN_BE.get(),
            (be, side) -> be.fluids);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.STEAMER_BE.get(),
            dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerBlockEntity::getItemCap);
        if (ModList.get().isLoaded("curios")) {
            dev.xkmc.youkaishomecoming.compat.curios.CuriosCompat.registerCapabilities(event);
        }
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerProjectileBehavior(GLItems.FROZEN_FROG_COLD.get());
            DispenserBlock.registerProjectileBehavior(GLItems.FROZEN_FROG_WARM.get());
            DispenserBlock.registerProjectileBehavior(GLItems.FROZEN_FROG_TEMPERATE.get());
            DispenserBlock.registerProjectileBehavior(GLItems.FAIRY_ICE_CRYSTAL.get());
            dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager.init();
            dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager.prepareData();
            if (net.neoforged.fml.ModList.get().isLoaded("terrablender")) {
                dev.xkmc.youkaishomecoming.compat.terrablender.TerraBlenderCompat.registerBiomes();
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void gatherData(GatherDataEvent event) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, GLTagGen::onBlockTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, GLTagGen::onItemTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, GLTagGen::onEntityTagGen);
        REGISTRATE.addDataGenerator(GLStructureTagGen.BIOME_TAG, GLStructureTagGen::genBiomeTag);
        REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GLDataMapGen::dataMapGen);
        REGISTRATE.addDataGenerator(ProviderType.LANG, GLLang::genLang);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, GLRecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.LOOT, GLStructureLootGen::genLoot);
        REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, GLAdvGen::genAdv);
        var init = REGISTRATE.getDataGenInitializer();
        GLStructureGen.init(init);
        init.add(Registries.NOISE, GLBiomes::registerNoise);
        init.add(Registries.PLACED_FEATURE, GLBiomes::registerPlacements);
        init.add(Registries.BIOME, GLBiomes::registerBiomes);
        new GLDamageTypes(REGISTRATE).generate();

        var gen = event.getGenerator();
        gen.addProvider(event.includeServer(), new GLGLMProvider(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(event.includeClient(), new dev.xkmc.youkaishomecoming.init.data.AdditionalModelProvider(gen.getPackOutput(), MODID));

        dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager.prepareData();
        ReportBlocksInStructure.report();
    }

    public static ResourceLocation loc(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }
}
