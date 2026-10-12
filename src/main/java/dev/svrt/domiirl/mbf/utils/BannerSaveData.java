package dev.svrt.domiirl.mbf.utils;

//? if <1.21.6 {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

// Entities save to a CompoundTag before 1.21.6. Same "Banner" key and item format as ValueOutput, so worlds carry over
public final class BannerSaveData {

	private static final String KEY = "Banner";

	private BannerSaveData() {
	}

	public static Optional<ItemStack> read(CompoundTag tag, Entity entity) {
		Tag banner = tag.get(KEY);
		if (banner == null) {
			return Optional.empty();
		}
		//? if >=1.20.5 {
		return ItemStack.parse(entity.registryAccess(), banner);
		//?} else {
		/^return banner instanceof CompoundTag itemTag ? Optional.of(ItemStack.of(itemTag)) : Optional.empty();
		^///?}
	}

	public static void write(CompoundTag tag, Entity entity, ItemStack itemStack) {
		if (!itemStack.isEmpty()) {
			//? if >=1.20.5 {
			tag.put(KEY, itemStack.save(entity.registryAccess()));
			//?} else {
			/^tag.put(KEY, itemStack.save(new CompoundTag()));
			^///?}
		}
	}
}
*///?}
