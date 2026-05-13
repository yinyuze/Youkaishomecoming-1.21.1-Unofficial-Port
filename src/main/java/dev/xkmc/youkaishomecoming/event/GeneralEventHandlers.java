package dev.xkmc.youkaishomecoming.event;

import dev.xkmc.danmakuapi.api.DanmakuDamageEvent;
import dev.xkmc.danmakuapi.api.DanmakuUseEvent;
import dev.xkmc.danmakuapi.content.item.DanmakuItem;
import dev.xkmc.danmakuapi.content.item.LaserItem;
import dev.xkmc.youkaishomecoming.compat.curios.CuriosCompat;
import dev.xkmc.youkaishomecoming.content.attachment.misc.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.item.character.ReimuHairbandItem;
import dev.xkmc.youkaishomecoming.content.item.character.TouhouHatItem;
import dev.xkmc.youkaishomecoming.content.item.character.TouhouWingsItem;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLAdvGen;
import dev.xkmc.youkaishomecoming.init.data.GLDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.data.GLTagGen;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.GLMeta;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GeneralEventHandlers {

    @SubscribeEvent
    public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (player.isCreative() || player.isSpectator()) return;
        boolean wearingHairband = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ReimuHairbandItem
                || (ModList.get().isLoaded("curios") && CuriosCompat.hasCurioHairband(player));
        boolean wingsAndFairy = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof TouhouWingsItem
                && player.hasEffect(YHEffects.FAIRY);
        if (wingsAndFairy && !player.getAbilities().mayfly) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
        if (!wearingHairband && !wingsAndFairy && player.getAbilities().mayfly) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (event.getDamageSource().is(GLDamageTypes.KOISHI)) {
            if (event.getEntity() instanceof Player player) {
                GLMeta.KOISHI_ATTACK.type().getOrCreate(player).onBlock(player);
            }
        }
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Frog frog) {
            FrogGodCapability.startTracking(frog, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onDanmakuDamageType(DanmakuDamageEvent event) {
        var le = event.getUser();
        ItemStack stack = le.getItemBySlot(EquipmentSlot.HEAD);
        if (stack.getItem() instanceof TouhouHatItem hat) {
            event.setSource(hat.modifyDamageType(stack, le, event.getBullet(), event.getSource()));
            return;
        }
        if (le instanceof Player player) {
            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.applyDanmakuFromCurios(player, event);
            }
        }
    }

    @SubscribeEvent
    public static void onVillagerHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Villager)) return;
        if (!GLModConfig.SERVER.reimuSummonFlesh.get()) return;
        if (!(event.getSource().getEntity() instanceof ServerPlayer sp)) return;
        if (!isYoukai(sp)) return;
        ReimuEventHandlers.hurtWarn(sp);
    }

    @SubscribeEvent
    public static void onVillagerKilled(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Villager)) return;
        if (!GLModConfig.SERVER.reimuSummonFlesh.get()) return;
        if (!(event.getSource().getEntity() instanceof LivingEntity le)) return;
        if (!isYoukai(le)) return;
        ReimuEventHandlers.triggerReimuResponse(le, 16, false);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void collectBlood(LivingDeathEvent event) {
        if (!event.getEntity().getType().is(GLTagGen.FLESH_SOURCE)) return;
        if (event.getSource().getEntity() instanceof LivingEntity le) {
            boolean hasKnife = le.getMainHandItem().is(TagRef.TOOLS_KNIVES);
            boolean youkai = isYoukai(le);
            boolean hasRumia = hasRumiaHairband(le);
            if ((hasKnife && youkai) || hasRumia) {
                spawnBlood(le);
            }
        }
    }

    private static boolean isYoukai(LivingEntity e) {
        return e.hasEffect(YHEffects.YOUKAIFYING) ||
                e.hasEffect(YHEffects.YOUKAIFIED);
    }

    private static boolean hasRumiaHairband(LivingEntity le) {
        if (le.getItemBySlot(EquipmentSlot.HEAD).is(GLItems.RUMIA_HAIRBAND.asItem())) return true;
        if (le instanceof Player player && ModList.get().isLoaded("curios")) {
            return CuriosCompat.hasCurioItem(player, GLItems.RUMIA_HAIRBAND.asItem());
        }
        return false;
    }

    @SubscribeEvent
    public static void onDanmakuUse(DanmakuUseEvent event) {
        Player player = event.getPlayer();
        ItemStack ammo = event.getStack();
        net.minecraft.world.item.DyeColor color = null;
        if (ammo.getItem() instanceof DanmakuItem d) color = d.color;
        else if (ammo.getItem() instanceof LaserItem l) color = l.color;
        if (color == null) return;

        if (anySupportingHat(player, color)) {
            event.setConsume(false);
            event.setCooldown(event.getCooldown() / 2);
        }
    }

    private static boolean anySupportingHat(Player player, net.minecraft.world.item.DyeColor color) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (head.getItem() instanceof TouhouHatItem h && h.support(color)) return true;
        if (ModList.get().isLoaded("curios")) {
            return CuriosCompat.anyCurioHatSupports(player, color);
        }
        return false;
    }

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (!event.getAdvancement().id().equals(GLAdvGen.FEED_REIMU)) return;
        event.getEntity().getInventory().add(new ItemStack(java.util.Objects.requireNonNull(GLItems.REIMU_HAIRBAND.get())));
    }

    @SubscribeEvent
    public static void onInteractParrot(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof net.minecraft.world.entity.animal.Parrot parrot
                && event.getItemStack().is(YHFood.RAW_LAMPREY.item.get())) {
            if (!parrot.level().isClientSide()) {
                var mystiaType = dev.xkmc.youkaishomecoming.init.registrate.GLEntities.MYSTIA.get();
                var mystia = mystiaType.create(parrot.level());
                if (mystia != null) {
                    mystia.moveTo(parrot.position());
                    mystia.initSpellCard();
                    parrot.level().addFreshEntity(mystia);
                    parrot.discard();
                }
                if (!event.getEntity().isCreative()) {
                    event.getItemStack().shrink(1);
                }
            }
            event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static void spawnBlood(LivingEntity le) {
        if (!le.getOffhandItem().is(Items.GLASS_BOTTLE)) return;
        le.getOffhandItem().shrink(1);
        if (le instanceof Player player) {
            player.getInventory().add(YHItems.BLOOD_BOTTLE.item().asStack(1));
        } else {
            le.spawnAtLocation(YHItems.BLOOD_BOTTLE.item().asStack(1));
        }
    }

}

