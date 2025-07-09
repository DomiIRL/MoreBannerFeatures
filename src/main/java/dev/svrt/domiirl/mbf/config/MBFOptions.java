package dev.svrt.domiirl.mbf.config;

import dev.svrt.domiirl.mbf.config.options.BooleanOption;

public class MBFOptions {

	public static final BooleanOption FOX_CORRECTION;
	public static final BooleanOption HORSE_SLOT;
	public static final BooleanOption HORSE_BANNERS;
	public static final BooleanOption BOAT_BANNERS;
	public static final BooleanOption MINECART_BANNERS;
	public static final BooleanOption STRIDER_BANNERS;
	public static final BooleanOption HAPPY_GHAST_BANNERS;
	public static final BooleanOption ALTERNATE_HAPPY_GHAST;
	public static final BooleanOption HANGING_BANNERS;
	public static final BooleanOption VILLAGER_BANNERS;
	public static final BooleanOption ERRORS;

	static {
		FOX_CORRECTION = new BooleanOption("fox_correction", true);
		HORSE_SLOT = new BooleanOption("horse_slot", true);
		HORSE_BANNERS = new BooleanOption("horse_banners", true);
		BOAT_BANNERS = new BooleanOption("boat_banners", true);
		MINECART_BANNERS = new BooleanOption("minecart_banners", false);
		STRIDER_BANNERS = new BooleanOption("strider_banners", true);
		HAPPY_GHAST_BANNERS = new BooleanOption("happy_ghast_banners", true);
		ALTERNATE_HAPPY_GHAST = new BooleanOption("alternate_happy_ghast_banners", false);
		HANGING_BANNERS = new BooleanOption("hanging_banners", true);
		VILLAGER_BANNERS = new BooleanOption("villager_banners", true);
		ERRORS = new BooleanOption("errors", true);
	}

}
