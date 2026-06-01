package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

  public static Item BANNER_THREAD;

  public static void init() {
    BANNER_THREAD = register("banner_thread", Item::new, new Item.Properties().component(ModDataComponents.MAX_BANNER_LAYERS, 1));

    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
      output.accept(new net.minecraft.world.item.ItemStack(BANNER_THREAD), net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    });
  }

  public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
    Identifier itemId = Identifier.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, name);
    Item item = itemFactory.apply(properties.setId(keyOfItem(name)));
    Registry.register(BuiltInRegistries.ITEM, itemId, item);
    return item;
  }

  private static ResourceKey<Item> keyOfItem(String name) {
    return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, name));
  }

}
