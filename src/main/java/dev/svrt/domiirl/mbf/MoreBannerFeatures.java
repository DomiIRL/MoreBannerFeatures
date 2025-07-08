package dev.svrt.domiirl.mbf;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "mbf";

	public static Boolean trinketsInstalled = null;

	@Override
	public void onInitialize() {
		MBFConfigManager.load();
		ModDataComponents.init();
		ModItems.init();
		ModRecipeSerializers.init();
	}

	public static boolean isTrinketsInstalled() {
		if (trinketsInstalled == null) {
			trinketsInstalled = FabricLoader.INSTANCE.getAllMods().stream().anyMatch(modContainer -> modContainer.getMetadata().getId().equalsIgnoreCase("trinkets"));
		}
		return trinketsInstalled;
	}


}
