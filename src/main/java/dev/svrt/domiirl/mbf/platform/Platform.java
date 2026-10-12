package dev.svrt.domiirl.mbf.platform;

import java.nio.file.Path;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else if neoforge {
/*import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
*///?} else {
/*import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
*///?}

public final class Platform {

	private Platform() {
	}

	public static boolean isModLoaded(String modId) {
		//? if fabric {
		return FabricLoader.getInstance().isModLoaded(modId);
		//?} else if forge && >=26.1 {
		/*return ModList.isLoaded(modId);
		*///?} else {
		/*return ModList.get().isLoaded(modId);
		*///?}
	}

	public static Path configDir() {
		//? if fabric {
		return FabricLoader.getInstance().getConfigDir();
		//?} else {
		/*return FMLPaths.CONFIGDIR.get();
		*///?}
	}

}
