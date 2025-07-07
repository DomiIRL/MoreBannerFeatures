package dev.svrt.domiirl.mbf;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "mbf";

	public static Boolean trinketsInstalled = null;

	@Override
	public void onInitialize() {
		MBFConfigManager.load();
		ModRecipes.init();
	}

	public static boolean isTrinketsInstalled() {
		if (trinketsInstalled == null) {
			trinketsInstalled = FabricLoader.INSTANCE.getAllMods().stream().anyMatch(modContainer -> modContainer.getMetadata().getId().equalsIgnoreCase("trinkets"));
		}
		return trinketsInstalled;
	}


}
