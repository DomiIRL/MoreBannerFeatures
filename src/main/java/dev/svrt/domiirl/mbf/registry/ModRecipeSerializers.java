package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.recipe.ArmorDecorationRecipe;
//? if <1.20.5 {
/*import dev.svrt.domiirl.mbf.recipe.BannerThreadRecipe;
*///?}
import dev.svrt.domiirl.mbf.recipe.UseBannerThreadsRecipe;
import dev.svrt.domiirl.mbf.platform.Platform;
import net.minecraft.core.Registry;
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?}
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {

  public static RecipeSerializer<ArmorDecorationRecipe> ARMOR_DECORATION;
  public static RecipeSerializer<UseBannerThreadsRecipe> USE_BANNER_THREADS;
  //? if <1.20.5 {
  /*public static RecipeSerializer<BannerThreadRecipe> BANNER_THREAD;
  *///?}

  public static void init() {
    ARMOR_DECORATION = register("crafting_special_armordecoration", ArmorDecorationRecipe.SERIALIZER);
    USE_BANNER_THREADS = register("crafting_special_use_banner_threads", UseBannerThreadsRecipe.SERIALIZER);
    //? if <1.20.5 {
    /*BANNER_THREAD = register("crafting_special_banner_thread", BannerThreadRecipe.SERIALIZER);
    *///?}
  }

  public static <T extends CustomRecipe> RecipeSerializer<T> register(String id, RecipeSerializer<T> serializer) {
    return Platform.register(BuiltInRegistries.RECIPE_SERIALIZER, MoreBannerFeatures.id(id), serializer);
  }
}
