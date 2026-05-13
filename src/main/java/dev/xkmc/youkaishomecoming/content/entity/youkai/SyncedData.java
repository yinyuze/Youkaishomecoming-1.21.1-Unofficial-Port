package dev.xkmc.youkaishomecoming.content.entity.youkai;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SyncedData {

	public static final EntityDataSerializer<Integer> INT = EntityDataSerializers.INT;
	public static final EntityDataSerializer<Boolean> BOOL = EntityDataSerializers.BOOLEAN;

	private final Function<EntityDataSerializer<?>, EntityDataAccessor<?>> factory;
	private final List<Entry<?>> list = new ArrayList<>();

	public SyncedData(Function<EntityDataSerializer<?>, EntityDataAccessor<?>> factory) {
		this.factory = factory;
	}

	public <T> EntityDataAccessor<T> define(EntityDataSerializer<T> ser, T def, String key) {
		@SuppressWarnings("unchecked")
		EntityDataAccessor<T> ans = (EntityDataAccessor<T>) factory.apply(ser);
		list.add(new Entry<>(ans, def, key));
		return ans;
	}

	public void write(FriendlyByteBuf buf) {
	}

	public void read(FriendlyByteBuf buf) {
	}

	public void write(CompoundTag tag) {
	}

	public void read(CompoundTag tag) {
	}

	private record Entry<T>(EntityDataAccessor<T> accessor, T def, String key) {
	}

}
