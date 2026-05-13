package dev.xkmc.youkaishomecoming.content.entity.characters.fairy;

import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.youkaishomecoming.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.youkaishomecoming.content.entity.behavior.sensor.YoukaiFindPreySensor;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.combat.YoukaiSearchTargetTask;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.core.TaskBoard;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.home.YoukaiCraftTask;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.play.ItemPickupTask;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.play.YoukaiHuntTask;
import dev.xkmc.youkaishomecoming.content.entity.module.*;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.IYoukaiMerchant;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiFlags;
import dev.xkmc.youkaishomecoming.content.item.ingredient.FrozenFrogItem;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.GLBrains;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SerialClass
public class CirnoEntity extends FairyEntity implements IYoukaiMerchant {

    public static AttributeSupplier.Builder createAttributes() {
        return GeneralYoukaiEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    public CirnoEntity(EntityType<? extends CirnoEntity> type, Level level) {
        super(type, level);
        setPersistenceRequired();
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected YoukaiCombatManager createCombatManager() {
        return new CirnoCombatManager(this);
    }

    @Override
    protected List<AbstractYoukaiModule> createModules() {
        return List.of(
                new HomeModule(this),
                new FeedModule(this),
                new TalkModule(this),
                new CountPickupModule(this, e -> e.getItem() instanceof FrozenFrogItem)
        );
    }

    @Override
    protected void constructTaskBoard(TaskBoard board) {
        super.constructTaskBoard(board);
        board.addExclusive(50, new FollowTemptation<>(), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());
        board.addExclusive(200, new ItemPickupTask(), Activity.IDLE, Activity.PLAY);
        board.addExclusive(250, new YoukaiHuntTask(6), GLBrains.HUNT.get());

        board.addRandom(new YoukaiCraftTask<>(this::doCraft, 60, 12000), GLBrains.AT_HOME.get());

        board.addBehaviorActivity(YoukaiSearchTargetTask.class, GLBrains.HUNT.get());

        board.addSensor(new ItemTemptingSensor<CirnoEntity>().setRadius(16, 8)
                .temptedWith((self, stack) -> stack.getItem() instanceof FrozenFrogItem)
                .setScanRate(e -> 20));

        board.addSensor(new NearbyItemsSensor<CirnoEntity>().setRadius(18, 6)
                .setScanRate(e -> e.playOrHunt() ? 20 : 60));
        board.addSensor(new YoukaiFindPreySensor<CirnoEntity>(e -> true)
                .setScanRate(e -> e.playOrHunt() ? 20 : 40));

        board.addPrioritizedActivity(GLBrains.HUNT.get(), GLBrains.MEM_PREY.get(), 200);
    }

    private boolean playOrHunt() {
        var a = getActivity();
        return a == Activity.PLAY || a == GLBrains.HUNT.get();
    }

    private ItemStack doCraft(boolean simulate) {
        var module = getModule(CountPickupModule.class);
        if (module.isEmpty()) return ItemStack.EMPTY;
        if (module.get().getCount() < 3) return ItemStack.EMPTY;
        if (!simulate) {
            module.get().consume(3);
        }
        return GLItems.FAIRY_ICE_CRYSTAL.asStack();
    }

    @Override
    public String getBrainDebugInfo() {
        int frogPickup = getModule(CountPickupModule.class)
                .map(CountPickupModule::getCount).orElse(0);
        if (frogPickup == 0) return super.getBrainDebugInfo();
        return super.getBrainDebugInfo() + "\n" + frogPickup + " frogs";
    }

    // merchant

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isAggressive()) return InteractionResult.PASS;
        boolean isYoukaified = player.hasEffect(YHEffects.YOUKAIFIED);
        boolean holdingFrog = player.getItemInHand(hand).getItem() instanceof FrozenFrogItem;
        if (!isYoukaified && !holdingFrog) return InteractionResult.PASS;
        if (player instanceof ServerPlayer sp) openMenu(sp);
        return InteractionResult.SUCCESS;
    }

    public void openMenu(Player player) {
        if (getTradingPlayer() != null && getTradingPlayer().isAlive()) return;
        tradingOffers = null;
        init(player);
        openTradingScreen(player, getName(), 0);
    }

    private Item getWantedItem() {
        Holder<Biome> holder = level().getBiome(this.blockPosition());
        long day = level().getGameTime() / 24000;
        double rand = RandomSource.create(RandomSource.create(day).nextLong()).nextDouble();
        if (holder.is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) {
            return rand < 0.3 ? GLItems.FROZEN_FROG_TEMPERATE.get() : GLItems.FROZEN_FROG_WARM.get();
        } else if (holder.is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) {
            return rand < 0.3 ? GLItems.FROZEN_FROG_TEMPERATE.get() : GLItems.FROZEN_FROG_COLD.get();
        } else {
            return rand < 0.5 ? GLItems.FROZEN_FROG_WARM.get() : GLItems.FROZEN_FROG_COLD.get();
        }
    }

    private MerchantOffers buildOfferList() {
        MerchantOffers ans = new MerchantOffers();
        var item = getWantedItem();
        ans.add(offer(YHFood.CANDY_APPLE.item.get(), 4, YHFood.FAIRY_CANDY.item.asStack()));
        ans.add(offer(item, 1, GLItems.FAIRY_ICE_CRYSTAL.asStack()));
        ans.add(offer(item, 1, DanmakuItems.Bullet.CIRCLE.get(DyeColor.CYAN).asStack(8)));
        ans.add(offer(item, 1, DanmakuItems.Bullet.BALL.get(DyeColor.CYAN).asStack(8)));
        ans.add(offer(item, 1, DanmakuItems.Bullet.MENTOS.get(DyeColor.CYAN).asStack(4)));
        if (!getFlag(YoukaiFlags.HAIRBAND_TRADED)) {
            ans.add(new MerchantOffer(new ItemCost(item, 16), Optional.empty(), GLItems.CIRNO_HAIRBAND.asStack(), 1, 0, 0));
        }
        return ans;
    }

    private static MerchantOffer offer(Item in, int a, ItemStack b) {
        return new MerchantOffer(new ItemCost(in, a), Optional.empty(), b, 64, 0, 0);
    }

    private MerchantOffers tradingOffers;
    private Player tradingPlayer;

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (tradingOffers == null) tradingOffers = buildOfferList();
        return tradingOffers;
    }

    @Override
    public void overrideOffers(MerchantOffers offers) {
        this.tradingOffers = offers;
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        IYoukaiMerchant.super.notifyTrade(offer);
        if (offer.getResult().is(GLItems.CIRNO_HAIRBAND.get())) {
            setFlag(YoukaiFlags.HAIRBAND_TRADED, true);
        }
    }

}

