package dev.svrt.domiirl.mbf;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;

public class MoreBannerFeaturesModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return MBFOptionsScreen::new;
	}

}
