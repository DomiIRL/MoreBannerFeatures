package dev.svrt.domiirl.mbf;

import dev.svrt.domiirl.mbf.platform.Platform;
import net.minecraft.resources.Identifier;

public class MoreBannerFeatures {

	public static final String MOD_ID = "mbf";

	public static Identifier id(String path) {
		//? if >=1.21 {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
		//?} else {
		/*return new Identifier(MOD_ID, path);
		*///?}
	}

	public static Boolean trinketsInstalled = null;
	public static Boolean accessoriesInstalled = null;
	public static Boolean curiosInstalled = null;

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

	public static boolean isCuriosInstalled() {
		if (curiosInstalled == null) {
			curiosInstalled = Platform.isModLoaded("curios");
		}
		return curiosInstalled;
	}


}
