//? if forge {
/*package dev.svrt.domiirl.mbf.forge;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.config.MBFConfigManager;
//? if >=1.20.5 {
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
//?}
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
//? if >=1.19.3 {
import net.minecraft.core.registries.Registries;
//?} else {
/^import net.minecraft.core.Registry;
^///?}
//? if >=1.19.3 {
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
//?}
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
//? if >=1.19 {
import net.minecraftforge.registries.RegisterEvent;
//?} else {
/^import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
^///?}

@Mod(MoreBannerFeatures.MOD_ID)
public class MoreBannerFeaturesForge {

	//? if >=1.19 {
	public MoreBannerFeaturesForge(FMLJavaModLoadingContext context) {
	//?} else {
	/^// Forge 40 only calls a no-argument mod constructor
	public MoreBannerFeaturesForge() {
		FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
	^///?}
		MBFConfigManager.load();

		//? if >=1.21.9 {
		RegisterEvent.getBus(context.getModBusGroup()).addListener(MoreBannerFeaturesForge::onRegister);
		BuildCreativeModeTabContentsEvent.BUS.addListener(MoreBannerFeaturesForge::onBuildCreativeTab);
		//?} else if >=1.21.6 {
		/^RegisterEvent.getBus(context.getModBusGroup()).addListener(MoreBannerFeaturesForge::onRegister);
		BuildCreativeModeTabContentsEvent.getBus(context.getModBusGroup()).addListener(MoreBannerFeaturesForge::onBuildCreativeTab);
		^///?} else if >=1.19.3 {
		/^context.getModEventBus().addListener(MoreBannerFeaturesForge::onRegister);
		context.getModEventBus().addListener(MoreBannerFeaturesForge::onBuildCreativeTab);
		^///?} else if >=1.19 {
		/^// Before 1.19.3 the thread picks its creative tab itself, see ModItems
		context.getModEventBus().addListener(MoreBannerFeaturesForge::onRegister);
		^///?} else {
		/^// Before 1.19 every registry fires its own generic event
		context.getModEventBus().addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> ModItems.init());
		context.getModEventBus().addGenericListener(RecipeSerializer.class, (RegistryEvent.Register<RecipeSerializer<?>> event) -> ModRecipeSerializers.init());
		^///?}

		// Forge has no client-only mod entrypoint; the call is only linked on the client, so the server never loads the screen classes
		if (FMLEnvironment.dist == Dist.CLIENT) {
			MoreBannerFeaturesForgeClient.init(context);
		}
	}

	//? if >=1.19 {
	// Vanilla registries only accept entries while their RegisterEvent runs
	private static void onRegister(RegisterEvent event) {
		//? if >=1.20.5 {
		if (event.getRegistryKey().equals(Registries.DATA_COMPONENT_TYPE)) {
			ModDataComponents.init();
		} else if (event.getRegistryKey().equals(Registries.ITEM)) {
		//?} else {
		/^if (event.getRegistryKey().equals(Registries.ITEM)) {
		^///?}
			ModItems.init();
		} else if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
			ModRecipeSerializers.init();
		}
	}
	//?}

	//? if >=1.19.3 {
	private static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
			event.accept(new ItemStack(ModItems.BANNER_THREAD), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
	//?}

}
*///?}
