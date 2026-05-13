package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;

public class TemptNotifyGoal<T extends Animal & StateMachineMob> extends TemptGoal implements INotifyMoveGoal {

	public final T deer;

	// Cache to avoid hitting Quark's FeedingTrough POI scan every tick.
	private int cachedTick = Integer.MIN_VALUE;
	private boolean cachedResult;

	public TemptNotifyGoal(T deer, double speed, Ingredient food, boolean canScare) {
		super(deer, speed, food, canScare);
		this.deer = deer;
	}

	private boolean cachedCanUse() {
		int tick = deer.tickCount;
		if (tick - cachedTick >= 20) {
			cachedTick = tick;
			cachedResult = super.canUse();
		}
		return cachedResult;
	}

	@Override
	public boolean canUse() {
		return deer.states().isMobile() && cachedCanUse();
	}

	public boolean wantsToMove() {
		return cachedCanUse();
	}

}
