//? if forge {
/*package dev.svrt.domiirl.mbf.forge;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(MoreBannerFeatures.MOD_ID)
public class MoreBannerFeaturesForge {

	public MoreBannerFeaturesForge(FMLJavaModLoadingContext context) {
		MBFConfigManager.load();

		// Vanilla registries only accept entries while their RegisterEvent runs
		RegisterEvent.getBus(context.getModBusGroup()).addListener(event -> {
			if (event.getRegistryKey().equals(Registries.DATA_COMPONENT_TYPE)) {
				ModDataComponents.init();
			} else if (event.getRegistryKey().equals(Registries.ITEM)) {
				ModItems.init();
			} else if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
				ModRecipeSerializers.init();
			}
		});

		BuildCreativeModeTabContentsEvent.BUS.addListener(event -> {
			if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
				event.accept(new ItemStack(ModItems.BANNER_THREAD), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			}
		});

		// Forge has no client-only mod entrypoint; the call is only linked on the client, so the server never loads the screen classes
		if (FMLEnvironment.dist == Dist.CLIENT) {
			MoreBannerFeaturesForgeClient.init(context);
		}
	}

}
*///?}
