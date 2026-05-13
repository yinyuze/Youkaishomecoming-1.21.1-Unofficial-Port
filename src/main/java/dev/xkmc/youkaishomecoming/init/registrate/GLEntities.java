package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaRenderer;
import dev.xkmc.youkaishomecoming.content.entity.characters.fairy.CirnoEntity;
import dev.xkmc.youkaishomecoming.content.entity.characters.fairy.CirnoRenderer;
import dev.xkmc.youkaishomecoming.content.entity.characters.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.entity.characters.maiden.*;
import dev.xkmc.youkaishomecoming.content.entity.characters.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.characters.rumia.RumiaRenderer;
import dev.xkmc.youkaishomecoming.content.entity.misc.FairyIce;
import dev.xkmc.youkaishomecoming.content.entity.misc.FrozenFrog;
import dev.xkmc.youkaishomecoming.content.entity.youkai.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiRenderer;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.loot.EntityLootGen;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class GLEntities {

	public static final EntityEntry<RumiaEntity> RUMIA;
	public static final EntityEntry<ReimuEntity> REIMU;
	public static final EntityEntry<CirnoEntity> CIRNO;
	public static final EntityEntry<TunaEntity> TUNA;
	public static final EntityEntry<BoarEntity> BOAR;
	public static final EntityEntry<CrabEntity> CRAB;
	public static final EntityEntry<DeerEntity> DEER;

	public static final EntityEntry<MaidenEntity> SANAE;
	public static final EntityEntry<MarisaEntity> MARISA;
	public static final EntityEntry<GeneralYoukaiEntity> MYSTIA;
	public static final EntityEntry<BossYoukaiEntity> YUKARI, KOISHI;
	public static final EntityEntry<FairyEntity> SUNNY, LUNA, STAR;

	public static final EntityEntry<FrozenFrog> FROZEN_FROG;
	public static final EntityEntry<FairyIce> FAIRY_ICE;

	static {

		GensokyoLegacy.REGISTRATE.defaultCreativeTab(CreativeModeTabs.OP_BLOCKS);

		{

			RUMIA = GensokyoLegacy.REGISTRATE
					.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
					.attributes(RumiaEntity::createAttributes)
					.renderer(() -> RumiaRenderer::new)
					.spawnEgg(0x413734, 0xA55064).build()
					.loot(EntityLootGen::rumia)
					.register();

			REIMU = GensokyoLegacy.REGISTRATE
					.entity("hakurei_reimu", ReimuEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> ReimuRenderer::new)
					.spawnEgg(0xa93937, 0xfaf5f2).build()
					.loot(EntityLootGen::reimu)
					.register();

			CIRNO = GensokyoLegacy.REGISTRATE
					.entity("cirno", CirnoEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(CirnoEntity::createAttributes)
					.renderer(() -> CirnoRenderer::new)
					.spawnEgg(0x5676af, 0xb6ecf1).build()
					.loot(EntityLootGen::cirno)
					.register();

			TUNA = GensokyoLegacy.REGISTRATE
					.entity("tuna", TunaEntity::new, MobCategory.WATER_AMBIENT)
					.properties(e -> e.sized(3F, 1.2F).clientTrackingRange(4))
					.spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
					.attributes(TunaEntity::createAttributes)
					.renderer(() -> TunaRenderer::new)
					.spawnEgg(0x1E4D8B, 0xC0C0C0).build()
					.loot(EntityLootGen::tuna)
					.register();

			BOAR = GensokyoLegacy.REGISTRATE
					.entity("boar", BoarEntity::new, MobCategory.CREATURE)
					.properties(e -> e.sized(1f, 1f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
					.attributes(BoarEntity::createAttributes)
					.renderer(() -> BoarRenderer::new)
					.spawnEgg(0x60554A, 0x3E342F).build()
					.loot(EntityLootGen::boar)
					.register();

			CRAB = GensokyoLegacy.REGISTRATE
					.entity("crab", CrabEntity::new, MobCategory.WATER_AMBIENT)
					.properties(e -> e.sized(0.6f, 0.3f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
					.attributes(CrabEntity::createAttributes)
					.renderer(() -> CrabRenderer::new)
					.spawnEgg(0x727e8b, 0xdbc297).build()
					.loot(EntityLootGen::crab)
					.register();

			DEER = GensokyoLegacy.REGISTRATE
					.entity("deer", DeerEntity::new, MobCategory.CREATURE)
					.properties(e -> e.sized(0.9f, 2f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
					.attributes(DeerEntity::createAttributes)
					.renderer(() -> DeerRenderer::new)
					.spawnEgg(0xc77e55, 0xe8ddd0).build()
					.loot(EntityLootGen::deer)
					.register();
		}

		{
			YUKARI = GensokyoLegacy.REGISTRATE
					.entity("yukari_yakumo", BossYoukaiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4B1442, 0xFFFFFF).build()
					.loot(EntityLootGen::yukari)
					.register();

			SANAE = GensokyoLegacy.REGISTRATE
					.entity("kochiya_sanae", MaidenEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4eaff9, 0xFFFFFF).build()
					.loot(EntityLootGen::sanae)
					.register();

			KOISHI = GensokyoLegacy.REGISTRATE
					.entity("komeiji_koishi", BossYoukaiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x88BA7F, 0x645856).build()
					.loot(EntityLootGen::koishi)
					.register();

			MARISA = GensokyoLegacy.REGISTRATE
					.entity("kirisame_marisa", MarisaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> MarisaRenderer::new)
					.spawnEgg(0x52403C, 0xFAF2EF).build()
					.loot(EntityLootGen::marisa)
					.register();

			MYSTIA = GensokyoLegacy.REGISTRATE
					.entity("mystia_lorelei", GeneralYoukaiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(GeneralYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x9B6D79, 0xF4BDAE).build()
					.loot(EntityLootGen::mystia)
					.register();

			SUNNY = GensokyoLegacy.REGISTRATE
					.entity("sunny_milk", FairyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0xB14435, 0xFCF5D8).build()
					.loot(EntityLootGen::fairy)
					.register();

			LUNA = GensokyoLegacy.REGISTRATE
					.entity("luna_child", FairyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0xFFF9DA, 0xA26B4F).build()
					.loot(EntityLootGen::fairy)
					.register();

			STAR = GensokyoLegacy.REGISTRATE
					.entity("star_sapphire", FairyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x353D95, 0x482E25).build()
					.loot(EntityLootGen::fairy)
					.register();
		}

		{

			FROZEN_FROG = GensokyoLegacy.REGISTRATE
					.<FrozenFrog>entity("frozen_frog", FrozenFrog::new, MobCategory.MISC)
					.properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10))
					.renderer(() -> ThrownItemRenderer::new)
					.register();

			FAIRY_ICE = GensokyoLegacy.REGISTRATE
					.<FairyIce>entity("fairy_ice_crystal", FairyIce::new, MobCategory.MISC)
					.properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10))
					.renderer(() -> ThrownItemRenderer::new)
					.register();

		}

	}

	public static void register() {

	}

}
