package dev.xkmc.youkaishomecoming.content.spell.card;

import dev.xkmc.danmakuapi.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.part.MystiaPart;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public class MystiaSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		if (tick % 40 == 0) {
			addTicker(new MystiaPart<>(tick / 40 % 2 * 2 - 1));
		}
		super.tick(holder);
	}

}
