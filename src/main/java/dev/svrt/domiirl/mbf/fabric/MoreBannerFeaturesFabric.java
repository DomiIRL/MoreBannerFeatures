//? if fabric {
package dev.svrt.domiirl.mbf.fabric;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;
//? if >=26.1 {
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
//?} else {
/*import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
*///?}
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public class MoreBannerFeaturesFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		MBFConfigManager.load();
		ModDataComponents.init();
		ModItems.init();
		ModRecipeSerializers.init();

		//? if >=26.1 {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output ->
		//?} else {
		/*ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(output ->
		*///?}
			output.accept(new ItemStack(ModItems.BANNER_THREAD), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
	}

}
//?}
