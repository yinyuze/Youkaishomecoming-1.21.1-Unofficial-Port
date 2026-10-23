package dev.xkmc.youkaishomecoming.content.attachment.graze;

import dev.xkmc.danmakuapi.init.data.DanmakuTagGen;
import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.event.DanmakuGrazeEvent;
import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.init.data.GLModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.GLAttributes;
import dev.xkmc.youkaishomecoming.init.registrate.GLMeta;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

/**
 * GL-side implementation of the danmaku-battle player mechanics. Holds the static config/attribute
 * helpers used by {@link GrazeCapability}. {@code GrazeHelperMixin} routes the Danmaku API's
 * graze, hurt-gating, hit-box shrinking and shoot-forbidding hooks into these methods.
 */
public class GrazeHelperGL {

	public static final TagKey<Item> DANMAKU_SHOOTER = DanmakuTagGen.DANMAKU_SHOOTER;

	public static void graze(Player entity, GrazingEntity e) {
		var graze = GLMeta.GRAZE.type().getOrCreate(entity);
		if (graze.isInvul()) return;
		if (NeoForge.EVENT_BUS.post(new DanmakuGrazeEvent(entity, e)).isCanceled())
			return;
		if (graze.graze(entity) && entity instanceof ServerPlayer sp) {
			GensokyoLegacy.HANDLER.toClientPlayer(new GrazeToClient(0), sp);
		}
	}

	@Nullable
	public static LivingEntity getTarget(Player player) {
		return GLMeta.GRAZE.type().getOrCreate(player).findAny(player).orElse(null);
	}

	public static void addSession(Player player, LivingEntity target) {
		if (player.level().isClientSide()) return;
		if (!GLCharacterHelper.isFullCharacter(player)) return;
		if (!(target instanceof YoukaiEntity e)) return;
		if (e.targets.contains(player)) return;
		GLMeta.GRAZE.type().getOrCreate(player).initSession(player, e);
	}

	public static boolean forbidDanmaku(Player player) {
		var cap = GLMeta.GRAZE.type().getOrCreate(player);
		return cap.isInvul() || cap.isWeak();
	}

	public static void onDanmakuKill(Player player, YoukaiEntity e) {
		GLMeta.GRAZE.type().getOrCreate(player).stopSession(player, e.getUUID());
	}

	public static int getInitialResource(Player player) {
		return GLModConfig.SERVER.initialResource.get() +
				(int) player.getAttributeValue(attr(GLAttributes.INITIAL_RESOURCE));
	}

	public static int getInitialPower(Player player) {
		return GLModConfig.SERVER.initialPower.get() +
				(int) player.getAttributeValue(attr(GLAttributes.INITIAL_POWER));
	}

	public static int getMaxPower(Player player) {
		return GLModConfig.SERVER.danmakuMaxPower.get() +
				(int) player.getAttributeValue(attr(GLAttributes.MAX_POWER));
	}

	public static double getGrazeEffectiveness(Player player) {
		return GLModConfig.SERVER.grazeEffectiveness.get() +
				player.getAttributeValue(attr(GLAttributes.GRAZE_EFFECTIVENESS));
	}

	public static int getMaxResource(Player player) {
		return GLModConfig.SERVER.danmakuMaxResource.get() +
				(int) player.getAttributeValue(attr(GLAttributes.MAX_RESOURCE));
	}

	public static float getHitBoxDelta(Player player) {
		return (float) player.getAttributeValue(attr(GLAttributes.HITBOX));
	}

	@SuppressWarnings("unchecked")
	private static net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attr(
			dev.xkmc.l2core.init.reg.simple.Val<net.minecraft.world.entity.ai.attributes.Attribute> val) {
		return (net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.world.entity.ai.attributes.Attribute,
				net.minecraft.world.entity.ai.attributes.Attribute>) (Object) val.val();
	}

}
