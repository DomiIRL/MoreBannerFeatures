package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

  public static Item BANNER_THREAD;

  public static void init() {
    BANNER_THREAD = register("banner_thread", Item::new, new Item.Properties().component(ModDataComponents.MAX_BANNER_LAYERS, 1));

    ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
      entries.accept(BANNER_THREAD);
    });
  }

  public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
    ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, name);
    Item item = itemFactory.apply(properties.setId(keyOfItem(name)));
    Registry.register(BuiltInRegistries.ITEM, itemId, item);
    return item;
  }

  private static ResourceKey<Item> keyOfItem(String name) {
    return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, name));
  }

}
