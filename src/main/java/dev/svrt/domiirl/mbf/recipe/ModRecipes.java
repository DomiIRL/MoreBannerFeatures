package dev.svrt.domiirl.mbf.recipe;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {

  public static RecipeSerializer<ArmorDecorationRecipe> ARMOR_DECORATION = register("crafting_special_armordecoration", new CustomRecipe.Serializer<>(ArmorDecorationRecipe::new));

  public static void init() {

  }

  public static <T extends CustomRecipe> RecipeSerializer<T> register(String id, RecipeSerializer<T> serializer) {
    System.out.println("Registering recipe serializer: " + id);
    return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(MoreBannerFeatures.MOD_ID, id), serializer);
  }
}
