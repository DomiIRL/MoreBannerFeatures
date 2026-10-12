//? if forge {
/*package dev.svrt.domiirl.mbf.forge;

import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class MoreBannerFeaturesForgeClient {

	private MoreBannerFeaturesForgeClient() {
	}

	static void init(FMLJavaModLoadingContext context) {
		context.getContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
			() -> new ConfigScreenHandler.ConfigScreenFactory(MBFOptionsScreen::new));
	}

}
*///?}
