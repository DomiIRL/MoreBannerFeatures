//? if neoforge {
/*package dev.svrt.domiirl.mbf.neoforge;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.config.MBFConfigManager;
//? if >=1.20.5 {
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
//?}
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
//? if <1.20.5 {
/^import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
^///?}

@Mod(MoreBannerFeatures.MOD_ID)
public class MoreBannerFeaturesNeoForge {

	public MoreBannerFeaturesNeoForge(IEventBus modBus) {
		MBFConfigManager.load();

		// Vanilla registries only accept entries while their RegisterEvent runs
		modBus.addListener(RegisterEvent.class, event -> {
			//? if >=1.20.5 {
			if (event.getRegistryKey().equals(Registries.DATA_COMPONENT_TYPE)) {
				ModDataComponents.init();
			} else if (event.getRegistryKey().equals(Registries.ITEM)) {
			//?} else {
			/^if (event.getRegistryKey().equals(Registries.ITEM)) {
			^///?}
				ModItems.init();
			} else if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
				ModRecipeSerializers.init();
			}
		});

		modBus.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
			if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
				event.accept(new ItemStack(ModItems.BANNER_THREAD), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			}
		});

		//? if <1.20.5 {
		/^if (FMLEnvironment.dist == Dist.CLIENT) {
			MoreBannerFeaturesNeoForgeClient.init();
		}
		^///?}
	}

}
*///?}
