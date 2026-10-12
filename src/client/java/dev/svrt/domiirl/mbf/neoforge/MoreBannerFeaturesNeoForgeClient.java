//? if neoforge {
/*package dev.svrt.domiirl.mbf.neoforge;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = MoreBannerFeatures.MOD_ID, dist = Dist.CLIENT)
public class MoreBannerFeaturesNeoForgeClient {

	public MoreBannerFeaturesNeoForgeClient(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> new MBFOptionsScreen(parent));
	}

}
*///?}
