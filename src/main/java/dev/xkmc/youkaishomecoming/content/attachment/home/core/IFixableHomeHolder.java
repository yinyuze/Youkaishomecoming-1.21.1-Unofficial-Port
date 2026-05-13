package dev.xkmc.youkaishomecoming.content.attachment.home.core;

import dev.xkmc.youkaishomecoming.content.attachment.home.structure.BlockFix;
import dev.xkmc.youkaishomecoming.content.attachment.home.structure.FixStage;

import java.util.List;

public interface IFixableHomeHolder extends IHomeHolder {

	boolean isBroken();

	int doFix(int count, FixStage stage);

	List<BlockFix> popFix(int count, FixStage stage);

}
