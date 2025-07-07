package dev.svrt.domiirl.mbf.accessor;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public interface Bannerable {

	@NotNull
	ItemStack getBannerItem();

}
