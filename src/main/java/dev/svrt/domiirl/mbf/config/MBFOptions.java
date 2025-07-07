package dev.svrt.domiirl.mbf.config;

import dev.svrt.domiirl.mbf.config.options.BooleanOption;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.1.0
 */
public class MBFOptions {

	public static final BooleanOption FOX_CORRECTION;
	public static final BooleanOption HORSE_SLOT;
	public static final BooleanOption PIG_BANNERS;
	public static final BooleanOption BOAT_BANNERS;
	public static final BooleanOption STRIDER_BANNERS;
	public static final BooleanOption HANGING_BANNERS;
	public static final BooleanOption VILLAGER_BANNERS;
	public static final BooleanOption ERRORS;

	static {
		HANGING_BANNERS = new BooleanOption("hanging_banners", true);
		FOX_CORRECTION = new BooleanOption("fox_correction", true);
		HORSE_SLOT = new BooleanOption("horse_slot", true);
		BOAT_BANNERS = new BooleanOption("boat_banners", true);
		ERRORS = new BooleanOption("errors", true);

		PIG_BANNERS = new BooleanOption("pig_banners", true).display(false);
		STRIDER_BANNERS = new BooleanOption("strider_banners", true).display(false);
		VILLAGER_BANNERS = new BooleanOption("villager_banners", true).display(true);
	}

}
