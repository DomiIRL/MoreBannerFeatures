package dev.svrt.domiirl.mbf;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.1.0
 */
public class MoreBannerFeaturesModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return MBFOptionsScreen::new;
	}

}
