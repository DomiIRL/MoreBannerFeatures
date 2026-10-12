//? if fabric {
package dev.svrt.domiirl.mbf.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;

public class MoreBannerFeaturesModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return MBFOptionsScreen::new;
	}

}
//?}
