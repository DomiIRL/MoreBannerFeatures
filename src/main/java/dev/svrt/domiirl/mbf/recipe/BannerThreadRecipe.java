//? if <1.20.5 {
/*package dev.svrt.domiirl.mbf.recipe;

import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import dev.svrt.domiirl.mbf.utils.BannerItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//? if >=1.19.4 {
import net.minecraft.core.RegistryAccess;
//?}
//? if >=1.19.3 {
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
//?} else {
/^import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
^///?}
//? if <1.20.2 {
/^import net.minecraft.resources.ResourceLocation;
^///?}

// A shaped recipe can only give its result data from 1.20.5, so before that the threads are crafted here.
// The grid matches data/mbf/recipe/banner_thread_*.json: string and amethyst around iron or a diamond.
public class BannerThreadRecipe extends CustomRecipe {

	private static final String GRID = "SAS" + "AIA" + "SAS";

	//? if >=1.20.2 {
	public static final RecipeSerializer<BannerThreadRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(BannerThreadRecipe::new);

	public BannerThreadRecipe(CraftingBookCategory category) {
		super(category);
	}
	//?} else if >=1.19.3 {
	/^public static final RecipeSerializer<BannerThreadRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(BannerThreadRecipe::new);

	public BannerThreadRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}
	^///?} else {
	/^public static final RecipeSerializer<BannerThreadRecipe> SERIALIZER = new SimpleRecipeSerializer<>(BannerThreadRecipe::new);

	public BannerThreadRecipe(ResourceLocation id) {
		super(id);
	}
	^///?}

	@Override
	public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return ModRecipeSerializers.BANNER_THREAD;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public boolean matches(CraftingInput craftingInput, Level level) {
		return layers(craftingInput) > 0;
	}

	@Override
	//? if >=1.19.4 {
	public ItemStack assemble(CraftingInput craftingInput, RegistryAccess registryAccess) {
	//?} else {
	/^public ItemStack assemble(CraftingInput craftingInput) {
	^///?}
		int layers = layers(craftingInput);
		if (layers == 0) {
			return ItemStack.EMPTY;
		}
		ItemStack thread = new ItemStack(ModItems.BANNER_THREAD);
		BannerItems.setMaxLayers(thread, layers);
		return thread;
	}

	// 0 when the grid is not a thread recipe, otherwise the layers the thread adds
	private static int layers(CraftingInput grid) {
		if (grid.getWidth() != 3 || grid.getHeight() != 3) {
			return 0;
		}
		for (int i = 0; i < GRID.length(); i++) {
			Item expected = switch (GRID.charAt(i)) {
				case 'S' -> Items.STRING;
				case 'A' -> Items.AMETHYST_SHARD;
				default -> null;
			};
			if (expected != null && !grid.getItem(i).is(expected)) {
				return 0;
			}
		}
		ItemStack center = grid.getItem(4);
		return center.is(Items.IRON_INGOT) ? 1 : center.is(Items.DIAMOND) ? 5 : 0;
	}
}
*///?}
