package dev.xkmc.youkaishomecoming.content.item.character;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CirnoHairbandItem extends TouhouHatItem {

    public CirnoHairbandItem(Properties properties) {
        super(properties, TouhouMat.CIRNO_HAIRBAND);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return GensokyoLegacy.loc("textures/entity/cirno.png");
    }

    @Override
    protected void tick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.FAIRY, 40, 0, true, true), player);
        }
    }

    @Override
    public void onHurtTarget(ItemStack head, DamageSource source, LivingEntity target) {
        if (source.is(Tags.DamageTypes.IS_MAGIC)) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            if (target.canFreeze()) {
                target.setTicksFrozen(target.getTicksFrozen() + 120);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
        if (showTooltip()) {
            list.add(GLLang.ITEM$OBTAIN.get().append(GLLang.ITEM$OBTAIN_CIRNO_HAIRBAND.get()));
            list.add(GLLang.ITEM$USAGE.get());
            list.add(GLLang.ITEM$USAGE_CIRNO_HAIRBAND.get());
            list.add(effectDesc(YHEffects.FAIRY));
            list.add(supportDesc(DyeColor.LIGHT_BLUE));
        } else {
            list.add(GLLang.ITEM$UNKNOWN.get());
        }
    }

    @Override
    public boolean support(DyeColor color) {
        return color == DyeColor.LIGHT_BLUE;
    }

}
