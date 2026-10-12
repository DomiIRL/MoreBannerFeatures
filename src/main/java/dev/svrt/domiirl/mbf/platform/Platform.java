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

//? if forge && <1.19.3 {
/*import net.minecraftforge.registries.RegistryManager;
*///?}
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

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

	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T value) {
		//? if forge && <1.19.3 {
		/*// Forge locks the vanilla registry views before 1.19.3; during RegisterEvent its own registries take the entry
		RegistryManager.ACTIVE.<V>getRegistry(registry.key()).register(id, value);
		return value;
		*///?} else {
		return Registry.register(registry, id, value);
		//?}
	}

	public static Path configDir() {
		//? if fabric {
		return FabricLoader.getInstance().getConfigDir();
		//?} else {
		/*return FMLPaths.CONFIGDIR.get();
		*///?}
	}

}
