package dev.svrt.domiirl.morebannerfeatures.accessor;

import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import net.minecraft.world.item.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public interface BannerRenderState extends Bannerable {

	void setBannerItem(ItemStack stack);

}
