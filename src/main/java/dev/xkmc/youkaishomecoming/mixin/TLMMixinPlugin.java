package dev.xkmc.youkaishomecoming.mixin;

import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class TLMMixinPlugin implements IMixinConfigPlugin {

	private static final String TLM_MOD_ID = "touhou_little_maid";

	private static Boolean tlmLoaded;

	private static boolean isTlmLoaded() {
		if (tlmLoaded == null) {
			try {
				tlmLoaded = LoadingModList.get().getModFileById(TLM_MOD_ID) != null;
			} catch (Throwable t) {
				tlmLoaded = false;
			}
		}
		return tlmLoaded;
	}

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return isTlmLoaded();
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
