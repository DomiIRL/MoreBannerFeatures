package dev.svrt.domiirl.mbf;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "mbf";

	public static Boolean trinketsInstalled = null;
	public static Boolean accessoriesInstalled = null;

	@Override
	public void onInitialize() {
		MBFConfigManager.load();
		ModDataComponents.init();
		ModItems.init();
		ModRecipeSerializers.init();

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {

		});

		LootTableEvents.MODIFY.register((resourceKey, builder, lootTableSource, provider) -> {
			if (!lootTableSource.isBuiltin() || !resourceKey.location().getPath().contains("banner")) {
				return;
			}
			builder.modifyPools(poolBuilder -> {
				poolBuilder.apply(
					CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
						.include(ModDataComponents.MAX_BANNER_LAYERS)
						.build());
			});

		});
	}

	public static boolean isTrinketsInstalled() {
		if (trinketsInstalled == null) {
			trinketsInstalled = FabricLoader.getInstance().getAllMods().stream().anyMatch(modContainer -> modContainer.getMetadata().getId().equalsIgnoreCase("trinkets"));
		}
		return trinketsInstalled;
	}

	public static boolean isAccessoriesInstalled() {
		if (accessoriesInstalled == null) {
			accessoriesInstalled = FabricLoader.getInstance().getAllMods().stream().anyMatch(modContainer -> modContainer.getMetadata().getId().equalsIgnoreCase("accessories"));
		}
		return accessoriesInstalled;
	}


}
