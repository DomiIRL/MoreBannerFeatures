package dev.svrt.domiirl.morebannerfeatures;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.svrt.domiirl.morebannerfeatures.screen.MBFOptionsScreen;

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
