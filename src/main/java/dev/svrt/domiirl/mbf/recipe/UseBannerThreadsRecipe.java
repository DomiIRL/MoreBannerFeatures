package dev.svrt.domiirl.mbf.recipe;

import dev.svrt.domiirl.mbf.utils.BannerItems;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//? if >=26.1 {
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
//?} else if >=1.20.5 {
/*import net.minecraft.core.HolderLookup;
*///?}
//? if <26.1 && >=1.19.3 {
/*import net.minecraft.world.item.crafting.CraftingBookCategory;
*///?}
//? if <1.21.2 && >=1.19.3 {
/*import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
*///?}
//? if <1.20.5 {
/*import net.minecraft.core.RegistryAccess;
*///?}
//? if <1.20.2 {
/*import net.minecraft.resources.ResourceLocation;
*///?}
//? if <1.19.3 {
/*import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
*///?}

public class UseBannerThreadsRecipe extends CustomRecipe {

    public static final int MAX_BANNER_LAYERS = 16, VANILLA_MAX_BANNER_LAYERS = 6;

    //? if >=26.1 {
    public static final MapCodec<UseBannerThreadsRecipe> MAP_CODEC = MapCodec.unit(UseBannerThreadsRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, UseBannerThreadsRecipe> STREAM_CODEC = StreamCodec.unit(new UseBannerThreadsRecipe());
    public static final RecipeSerializer<UseBannerThreadsRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public UseBannerThreadsRecipe() {
        super();
    }
    //?} else if >=1.21.2 {
    /*public static final RecipeSerializer<UseBannerThreadsRecipe> SERIALIZER = new CustomRecipe.Serializer<>(UseBannerThreadsRecipe::new);

    public UseBannerThreadsRecipe(CraftingBookCategory category) {
        super(category);
    }
    *///?} else if >=1.20.2 {
    /*public static final RecipeSerializer<UseBannerThreadsRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(UseBannerThreadsRecipe::new);

    public UseBannerThreadsRecipe(CraftingBookCategory category) {
        super(category);
    }
    *///?} else if >=1.19.3 {
    /*public static final RecipeSerializer<UseBannerThreadsRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(UseBannerThreadsRecipe::new);

    // Recipes carried their own id until 1.20.2
    public UseBannerThreadsRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }
    *///?} else {
    /*public static final RecipeSerializer<UseBannerThreadsRecipe> SERIALIZER = new SimpleRecipeSerializer<>(UseBannerThreadsRecipe::new);

    // No crafting book categories before 1.19.3
    public UseBannerThreadsRecipe(ResourceLocation id) {
        super(id);
    }
    *///?}

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.USE_BANNER_THREADS;
    }

    //? if <1.21.2 {
    /*@Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
    *///?}

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        ItemStack banner = ItemStack.EMPTY;
        int threadLayerSum = 0;
        int itemCount = 0;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (stack.getItem() instanceof BannerItem) {
                    if (!banner.isEmpty()) return false; // Multiple banners
                    banner = stack;
                } else if (stack.getItem() == ModItems.BANNER_THREAD) {
                    int threadLayers = BannerItems.maxLayers(stack, 1);
                    threadLayerSum += threadLayers;
                } else {
                    return false; // Invalid item
                }
            }
        }

        // Must have exactly one banner and at least one thread
        return !banner.isEmpty() && threadLayerSum > 0;
    }

    @Override
    //? if >=26.1 {
    public ItemStack assemble(CraftingInput craftingInput) {
    //?} else if >=1.20.5 {
    /*public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider registryAccess) {
    *///?} else if >=1.19.4 {
    /*public ItemStack assemble(CraftingInput craftingInput, RegistryAccess registryAccess) {
    *///?} else {
    /*public ItemStack assemble(CraftingInput craftingInput) {
    *///?}
        ItemStack banner = ItemStack.EMPTY;
        int threadLayerSum = 0;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    banner = stack;
                } else if (stack.getItem() == ModItems.BANNER_THREAD) {
                    int threadLayers = BannerItems.maxLayers(stack, 1);
                    threadLayerSum += threadLayers;
                }
            }
        }

        if (banner.isEmpty() || threadLayerSum == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack result = banner.copy();
        result.setCount(1);

        int currentMax = BannerItems.maxLayers(result, VANILLA_MAX_BANNER_LAYERS);
        int newMax = Math.min(currentMax + threadLayerSum, MAX_BANNER_LAYERS);

        int layersUsed = Math.max(0, newMax - currentMax);
        if (layersUsed == 0) {
            return ItemStack.EMPTY;
        }

        BannerItems.setMaxLayers(result, newMax);

        return result;
    }

    @Override
    public net.minecraft.core.NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
        net.minecraft.core.NonNullList<ItemStack> remaining = net.minecraft.core.NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);

        int currentMax = VANILLA_MAX_BANNER_LAYERS;
        int[] threadLayers = new int[craftingInput.size()];

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    if (BannerItems.hasMaxLayers(stack)) {
                        currentMax = BannerItems.maxLayers(stack, 6);
                    }
                } else if (stack.getItem() == ModItems.BANNER_THREAD) {
                    threadLayers[i] = BannerItems.maxLayers(stack, 1);
                }
            }
        }

        int layersToUse = Math.max(0, Math.min(MAX_BANNER_LAYERS - currentMax, java.util.Arrays.stream(threadLayers).sum()));

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.BANNER_THREAD) {
                int threadLayer = threadLayers[i];
                if (layersToUse >= threadLayer) {
                    layersToUse -= threadLayer;
                } else if (layersToUse > 0) {
                    ItemStack leftover = new ItemStack(ModItems.BANNER_THREAD);
                    BannerItems.setMaxLayers(leftover, threadLayer - layersToUse);
                    remaining.set(i, leftover);
                    layersToUse = 0;
                } else {
                    //? if >=1.20 {
                    remaining.set(i, stack.copyWithCount(1));
                    //?} else {
                    /*ItemStack single = stack.copy();
                    single.setCount(1);
                    remaining.set(i, single);
                    *///?}
                }
            }
        }

        return remaining;
    }
}
