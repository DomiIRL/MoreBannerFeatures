//? if neoforge {
/*package dev.svrt.domiirl.mbf.neoforge;

import dev.svrt.domiirl.mbf.compat.WaveyCapesSupport;
import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;
//? if >=1.20.5 {
import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else {
/^import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.ConfigScreenHandler;
^///?}

//? if >=1.20.5 {
@Mod(value = MoreBannerFeatures.MOD_ID, dist = Dist.CLIENT)
public class MoreBannerFeaturesNeoForgeClient {

	public MoreBannerFeaturesNeoForgeClient(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> new MBFOptionsScreen(parent));
		WaveyCapesSupport.register();
	}

}
//?} else {
/^// Before 20.5 there are no client-only mod entrypoints, so the common one calls this on the client
public final class MoreBannerFeaturesNeoForgeClient {

	private MoreBannerFeaturesNeoForgeClient() {
	}

	static void init() {
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
			() -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new MBFOptionsScreen(parent)));
		WaveyCapesSupport.register();
	}

}
^///?}
*///?}
