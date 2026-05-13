package dev.xkmc.youkaishomecoming.content.entity.characters.maiden;

import dev.xkmc.youkaishomecoming.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.youkaishomecoming.content.entity.youkai.BossYoukaiEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@SerialClass
public class MaidenEntity extends BossYoukaiEntity {

	public MaidenEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		navCtrl.markHuman();
		setPersistenceRequired();
	}

	@Override
	protected YoukaiCombatManager createCombatManager() {
		return new MaidenCombatManager(this);
	}

}
