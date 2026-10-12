package dev.svrt.domiirl.mbf.utils;

//? if <1.21.6 {
/*import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

// Entities save to a CompoundTag before 1.21.6. Same "Banner" key and item format as ValueOutput, so worlds carry over
public final class BannerSaveData {

	private static final String KEY = "Banner";

	private BannerSaveData() {
	}

	public static Optional<ItemStack> read(CompoundTag tag, HolderLookup.Provider registries) {
		Tag banner = tag.get(KEY);
		return banner == null ? Optional.empty() : ItemStack.parse(registries, banner);
	}

	public static void write(CompoundTag tag, HolderLookup.Provider registries, ItemStack itemStack) {
		if (!itemStack.isEmpty()) {
			tag.put(KEY, itemStack.save(registries));
		}
	}
}
*///?}
