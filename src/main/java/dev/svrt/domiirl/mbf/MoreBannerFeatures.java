package dev.svrt.domiirl.mbf;

import dev.svrt.domiirl.mbf.platform.Platform;

public class MoreBannerFeatures {

	public static final String MOD_ID = "mbf";

	public static Boolean trinketsInstalled = null;
	public static Boolean accessoriesInstalled = null;

	public static boolean isTrinketsInstalled() {
		if (trinketsInstalled == null) {
			// isModLoaded honours provides aliases, the Trinkets fork only provides trinkets
			trinketsInstalled = Platform.isModLoaded("trinkets");
		}
		return trinketsInstalled;
	}

	public static boolean isAccessoriesInstalled() {
		if (accessoriesInstalled == null) {
			accessoriesInstalled = Platform.isModLoaded("accessories");
		}
		return accessoriesInstalled;
	}


}
