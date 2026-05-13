package dev.xkmc.youkaishomecoming.content.entity.characters.rumia;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.youkaishomecoming.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.combat.YoukaiUpdateTargetTask;
import dev.xkmc.youkaishomecoming.content.entity.behavior.task.core.TaskBoard;
import dev.xkmc.youkaishomecoming.content.entity.youkai.IYoukaiMerchant;
import dev.xkmc.youkaishomecoming.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiFeatureSet;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiFlags;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.GLBrains;
import dev.xkmc.youkaishomecoming.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

import java.util.Optional;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class RumiaEntity extends SmartYoukaiEntity implements IYoukaiMerchant {

    private static final EntityDimensions FALL = EntityDimensions.scalable(1.7f, 0.4f);
    private static final ResourceLocation EXRUMIA = GensokyoLegacy.loc("ex_rumia");

    @SerialField
    public final RumiaStateMachine state = new RumiaStateMachine(this);

    public RumiaEntity(EntityType<? extends RumiaEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, 8);
        setPersistenceRequired();
        sources.mobAttack = GLDamageTypes::rumia;
    }

    @Override
    protected void constructTaskBoard(TaskBoard board) {
        super.constructTaskBoard(board);
        board.addExclusive(50, new FollowTemptation<>(), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());
        board.addExclusive(0, new RumiaParalyzeGoal(), GLBrains.DOWN.get());

        board.addSensor(new ItemTemptingSensor<RumiaEntity>().setRadius(16, 8)
                .temptedWith((self, stack) -> stack.is(YHFood.FLESH_CHOCOLATE_MOUSSE.item.get()))
                .setScanRate(e -> 20));

        board.addPrioritizedActivity(GLBrains.DOWN.get(), GLBrains.MEM_DOWN.get(), -100);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "unsafe"})
    @Override
    public BrainActivityGroup<? extends SmartYoukaiEntity> getFightTasks() {
        return new BrainActivityGroup(Activity.FIGHT).priority(10).behaviours(
                new YoukaiUpdateTargetTask(),
                new RumiaAttackTask()
        ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return YoukaiEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    @Override
    public YoukaiFeatureSet getFeatures() {
        return isEx() ? YoukaiFeatureSet.BOSS : YoukaiFeatureSet.NONE;
    }

    @Override
    protected YoukaiCombatManager createCombatManager() {
        return new RumiaCombatManager(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        state.tick();
    }

    public boolean isCharged() {
        return state != null && isAlive() && state.isCharged();
    }

    public boolean isBlocked() {
        return state != null && isAlive() && state.isBlocked();
    }

    public boolean isEx() {
        return getFlag(YoukaiFlags.POWERED);
    }

    public void setEx(boolean ex) {
        var hp = getAttribute(Attributes.MAX_HEALTH);
        var atk = getAttribute(Attributes.ATTACK_DAMAGE);
        assert hp != null && atk != null;
        if (ex) {
            hp.addPermanentModifier(new AttributeModifier(EXRUMIA, 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            atk.addPermanentModifier(new AttributeModifier(EXRUMIA, 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            hp.removeModifier(EXRUMIA);
            atk.removeModifier(EXRUMIA);
        }
        setHealth(getMaxHealth());
        setFlag(YoukaiFlags.POWERED, ex);
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
        if (isCharged()) return;
        super.knockback(pStrength, pX, pZ);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        boolean isVoid = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        if (!isVoid && !isEx() && amount >= getMaxHealth()) {
            //if (GLModConfig.SERVER.exRumiaConversion.get())
            setEx(true);
        }
        if (source.getEntity() instanceof LivingEntity le) {
            state.onHurt(le, amount);
        }
        super.actuallyHurt(source, amount);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pPose) {
        return isBlocked() ? FALL.scale(getScale()) : super.getDefaultDimensions(pPose);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS_ID.equals(pKey)) {
            this.refreshDimensions();
        }
    }

    // hairband drop

    private boolean dropHairband = false;

    @Override
    public void die(DamageSource source) {
        dropHairband = isEx() && source.is(DanmakuDamageTypes.DANMAKU_TYPE) && source.getEntity() instanceof Player;
        super.die(source);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (dropHairband && GLModConfig.SERVER.rumiaHairbandDrop.get()) {
            ItemStack stack = GLItems.RUMIA_HAIRBAND.asStack();
            stack.setDamageValue(stack.getMaxDamage() - 1);
            spawnAtLocation(stack);
        }
    }

    // merchant

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isAggressive()) return InteractionResult.PASS;
        if (!player.hasEffect(YHEffects.YOUKAIFIED)) return InteractionResult.PASS;
        if (player instanceof ServerPlayer sp) openMenu(this, sp);
        return InteractionResult.SUCCESS;
    }

    public void openMenu(RumiaEntity rumia, Player player) {
        if (tradingPlayer != null && tradingPlayer.isAlive()) return;
        init(player);
        openTradingScreen(player, rumia.getName(), 0);
    }

    private static MerchantOffers getOfferList() {
        MerchantOffers ans = new MerchantOffers();
        ans.add(new MerchantOffer(new ItemCost(YHFood.COOKED_FLESH.item.get()), Optional.empty(), YHFood.FLESH.item.asStack(), 64, 0, 0));
        ans.add(offer(4, DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).asStack(8)));
        ans.add(offer(4, DanmakuItems.Bullet.BALL.get(DyeColor.RED).asStack(8)));
        ans.add(offer(4, DanmakuItems.Bullet.BUTTERFLY.get(DyeColor.RED).asStack(8)));
        ans.add(offer(4, DanmakuItems.Bullet.SPARK.get(DyeColor.RED).asStack(8)));
        ans.add(offer(32, DanmakuItems.Laser.LASER.get(DyeColor.RED).asStack(8)));
        return ans;
    }

    private static MerchantOffer offer(int a, ItemStack b) {
        return new MerchantOffer(new ItemCost(YHFood.FLESH_CHOCOLATE_MOUSSE.item.get(), a), Optional.empty(), b, 64, 0, 0);
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
        if (tradingOffers == null) tradingOffers = getOfferList();
        return tradingOffers;
    }

    @Override
    public void overrideOffers(MerchantOffers offers) {
        this.tradingOffers = offers;
    }

}