package dev.svrt.domiirl.mbf.compat;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;

import java.lang.reflect.Method;

public final class AccessoriesSupport {

	private AccessoriesSupport() {
	}

	// Accessories draws a small copy of every item without its own renderer onto the body,
	// on top of the cape and hat MBF already renders from those slots
	public static void register() {
		if (!MoreBannerFeatures.isAccessoriesInstalled()) return;
		try {
			Method registerNoRenderer = Class.forName("io.wispforest.accessories.api.client.AccessoriesRendererRegistry")
				.getMethod("registerNoRenderer", Item.class);
			for (Item item : BuiltInRegistries.ITEM) {
				if (item instanceof BannerItem) {
					registerNoRenderer.invoke(null, item);
				}
			}
		} catch (ReflectiveOperationException ignored) {
		}
	}

}
