//? if fabric {
package dev.svrt.domiirl.mbf.fabric;

import dev.svrt.domiirl.mbf.compat.WaveyCapesSupport;
import net.fabricmc.api.ClientModInitializer;

public class MoreBannerFeaturesFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		WaveyCapesSupport.register();
	}

}
//?}
