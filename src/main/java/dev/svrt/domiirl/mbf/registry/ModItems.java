package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.platform.Platform;
import net.minecraft.core.Registry;
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.world.item.CreativeModeTab;
*///?}
//? if >=1.21.2 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

  public static Item BANNER_THREAD;

  public static void init() {
    //? if >=1.20.5 {
    BANNER_THREAD = register("banner_thread", Item::new, new Item.Properties().component(ModDataComponents.MAX_BANNER_LAYERS, 1));
    //?} else if >=1.19.3 {
    /*// Before components a thread without the tag simply counts as one layer
    BANNER_THREAD = register("banner_thread", Item::new, new Item.Properties());
    *///?} else {
    /*// Creative tabs were picked on the item until 1.19.3
    BANNER_THREAD = register("banner_thread", Item::new, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    *///?}
  }

  public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
    Identifier itemId = MoreBannerFeatures.id(name);
    //? if >=1.21.2 {
    Item item = itemFactory.apply(properties.setId(keyOfItem(name)));
    //?} else {
    /*Item item = itemFactory.apply(properties);
    *///?}
    Platform.register(BuiltInRegistries.ITEM, itemId, item);
    return item;
  }

  //? if >=1.21.2 {
  private static ResourceKey<Item> keyOfItem(String name) {
    return ResourceKey.create(Registries.ITEM, MoreBannerFeatures.id(name));
  }
  //?}

}
