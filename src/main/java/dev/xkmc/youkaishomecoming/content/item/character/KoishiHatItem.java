package dev.xkmc.youkaishomecoming.content.item.character;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.GLEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KoishiHatItem extends TouhouHatItem {

    public KoishiHatItem(Properties properties) {
        super(properties, TouhouMat.KOISHI_HAT);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return GensokyoLegacy.loc("textures/model/koishi_hat.png");
    }

    @Override
    protected void tick(ItemStack stack, Level level, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) return;
        EffectUtil.refreshEffect(player, new MobEffectInstance(GLEffects.UNCONSCIOUS, 40, 0,
                true, true), player);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
        if (showTooltip()) {
            list.add(GLLang.ITEM$OBTAIN.get().append(GLLang.ITEM$OBTAIN_KOISHI_HAT.get(Component.literal("" + GLModConfig.SERVER.koishiAttackBlockCount.get()))));
            list.add(GLLang.ITEM$USAGE.get());
            list.add(effectDesc(GLEffects.UNCONSCIOUS));
            list.add(supportDesc(DyeColor.RED, DyeColor.BLUE));
        } else {
            list.add(GLLang.ITEM$UNKNOWN.get());
        }
    }

    @Override
    public boolean support(DyeColor color) {
        return color == DyeColor.RED || color == DyeColor.BLUE;
    }

}
