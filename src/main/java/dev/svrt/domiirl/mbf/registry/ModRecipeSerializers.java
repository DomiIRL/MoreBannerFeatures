package dev.svrt.domiirl.mbf.registry;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.recipe.ArmorDecorationRecipe;
import dev.svrt.domiirl.mbf.recipe.UseBannerThreadsRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {

  public static RecipeSerializer<ArmorDecorationRecipe> ARMOR_DECORATION;
  public static RecipeSerializer<UseBannerThreadsRecipe> USE_BANNER_THREADS = new CustomRecipe.Serializer<>(UseBannerThreadsRecipe::new);

  public static void init() {
    ARMOR_DECORATION = register("crafting_special_armordecoration", new CustomRecipe.Serializer<>(ArmorDecorationRecipe::new));
    USE_BANNER_THREADS = register("crafting_special_use_banner_threads", USE_BANNER_THREADS);
  }

  public static <T extends CustomRecipe> RecipeSerializer<T> register(String id, RecipeSerializer<T> serializer) {
    return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, id), serializer);
  }
}
