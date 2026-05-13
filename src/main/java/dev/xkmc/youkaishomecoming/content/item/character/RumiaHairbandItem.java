package dev.xkmc.youkaishomecoming.content.item.character;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RumiaHairbandItem extends TouhouHatItem {

    public RumiaHairbandItem(Properties properties) {
        super(properties, TouhouMat.RUMIA_HAIRBAND);
    }

    @Override
    protected void addModifiers(ItemAttributeModifiers.Builder builder) {
        builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(GensokyoLegacy.loc("rumia_hairband"), 0.25,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.HEAD);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return GensokyoLegacy.loc("textures/entity/rumia.png");
    }

    @Override
    protected void tick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide() && !player.hasEffect(YHEffects.SOBER)) {
            EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.YOUKAIFYING, 40, 0, true, true), player);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
        if (showTooltip()) {
            list.add(GLLang.ITEM$OBTAIN.get().append(GLLang.ITEM$OBTAIN_RUMIA_HAIRBAND.get()));
            list.add(GLLang.ITEM$USAGE.get());
            list.add(GLLang.ITEM$USAGE_RUMIA_HAIRBAND.get());
            list.add(effectDesc(YHEffects.YOUKAIFYING));
            list.add(supportDesc(DyeColor.BLACK));
        } else {
            list.add(GLLang.ITEM$UNKNOWN.get());
        }
    }

    @Override
    public boolean support(DyeColor color) {
        return color == DyeColor.BLACK;
    }

}
