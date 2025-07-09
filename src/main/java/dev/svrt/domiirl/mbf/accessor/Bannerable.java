package dev.svrt.domiirl.mbf.accessor;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Bannerable {

	@NotNull
	ItemStack moreBannerFeatures$getBannerItem();

	void moreBannerFeatures$setBannerItem(@NotNull ItemStack stack);

	default boolean moreBannerFeatures$isEnabled() {
		return true;
	}

}
