package dev.xkmc.youkaishomecoming.content.item.character;

import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CirnoWingsItem extends TouhouWingsItem {

    public CirnoWingsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
        if (TouhouHatItem.showTooltip()) {
            list.add(GLLang.ITEM$USAGE_FAIRY_WINGS.get(
                    Component.translatable(YHEffects.FAIRY.value().getDescriptionId())));
        } else {
            list.add(GLLang.ITEM$UNKNOWN.get());
        }
    }

}
