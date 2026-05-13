package dev.xkmc.youkaishomecoming.content.item.character;

import com.google.common.collect.Multimap;
import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.registrate.GLAttributes;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReimuHairbandItem extends TouhouHatItem {

    public ReimuHairbandItem(Properties properties) {
        super(properties, TouhouMat.REIMU_HAIRBAND);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addModifiers(ItemAttributeModifiers.Builder builder) {
        builder.add((DeferredHolder<Attribute, Attribute>) (Object) GLAttributes.HITBOX.val(),
                new AttributeModifier(GensokyoLegacy.loc("reimu_hairband_hitbox"), -0.1,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD);
        builder.add((DeferredHolder<Attribute, Attribute>) (Object) GLAttributes.INITIAL_RESOURCE.val(),
                new AttributeModifier(GensokyoLegacy.loc("reimu_hairband_resource"), 1,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCurioModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, Object slotContext, ResourceLocation id) {
        map.put((DeferredHolder<Attribute, Attribute>) (Object) GLAttributes.HITBOX.val(),
                new AttributeModifier(GensokyoLegacy.loc("reimu_hairband_hitbox_curio"), -0.1,
                        AttributeModifier.Operation.ADD_VALUE));
        map.put((DeferredHolder<Attribute, Attribute>) (Object) GLAttributes.INITIAL_RESOURCE.val(),
                new AttributeModifier(GensokyoLegacy.loc("reimu_hairband_resource_curio"), 1,
                        AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return GensokyoLegacy.loc("textures/entity/reimu.png");
    }

    @Override
    public DamageSource modifyDamageType(ItemStack stack, LivingEntity le, IDanmakuEntity danmaku, DamageSource type) {
        return DanmakuDamageTypes.abyssal(danmaku);
    }

    @Override
    protected void tick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide() && !player.getAbilities().mayfly && !player.isCreative() && !player.isSpectator()) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        if (showTooltip()) {
            list.add(GLLang.ITEM$OBTAIN.get().append(GLLang.ITEM$OBTAIN_REIMU_HAIRBAND.get()));
            list.add(GLLang.ITEM$USAGE.get());
            list.add(GLLang.ITEM$USAGE_REIMU_HAIRBAND.get());
            list.add(supportDesc(DyeColor.RED));
        } else {
            list.add(GLLang.ITEM$UNKNOWN.get());
        }
    }

    @Override
    public boolean support(DyeColor color) {
        return color == DyeColor.RED;
    }

}
