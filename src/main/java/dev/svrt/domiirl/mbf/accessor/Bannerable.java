package dev.svrt.domiirl.mbf.accessor;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Bannerable {

	@NotNull
	ItemStack mbf$getBannerItem();

	void mbf$setBannerItem(@NotNull ItemStack stack);

	default boolean mbf$isEnabled() {
		return true;
	}

}
