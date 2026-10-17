//? if forge {
/*package dev.svrt.domiirl.mbf.forge;

//? if <1.21.11 {
/^import dev.svrt.domiirl.mbf.compat.WaveyCapesSupport;
^///?}
import dev.svrt.domiirl.mbf.screen.MBFOptionsScreen;
//? if >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler;
//?} else {
/^import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
^///?}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class MoreBannerFeaturesForgeClient {

	private MoreBannerFeaturesForgeClient() {
	}

	static void init(FMLJavaModLoadingContext context) {
		//? if <1.21.11 {
		/^WaveyCapesSupport.register();
		^///?}
		//? if >=1.19 {
		context.getContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
			() -> new ConfigScreenHandler.ConfigScreenFactory(MBFOptionsScreen::new));
		//?} else {
		/^ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
			() -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, parent) -> new MBFOptionsScreen(parent)));
		^///?}
	}

}
*///?}
