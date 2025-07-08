package dev.svrt.domiirl.mbf.recipe;

import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UseBannerThreadsRecipe extends CustomRecipe {

    public static final int MAX_BANNER_LAYERS = 16, VANILLA_MAX_BANNER_LAYERS = 6;

    public UseBannerThreadsRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.ARMOR_DECORATION;
    }

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
                    int threadLayers = stack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 1);
                    threadLayerSum += threadLayers;
                } else {
                    return false; // Invalid item
                }
            }
        }

        // Must have exactly one banner and at least one thread
        return !banner.isEmpty() && threadLayerSum > 0 && itemCount == (1 + (itemCount - 1));
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider registryAccess) {
        ItemStack banner = ItemStack.EMPTY;
        int threadLayerSum = 0;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    banner = stack;
                } else if (stack.getItem() == ModItems.BANNER_THREAD) {
                    int threadLayers = stack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 1);
                    threadLayerSum += threadLayers;
                }
            }
        }

        if (banner.isEmpty() || threadLayerSum == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack result = banner.copy();
        result.setCount(1);

        int currentMax = result.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, VANILLA_MAX_BANNER_LAYERS);
        int newMax = Math.min(currentMax + threadLayerSum, MAX_BANNER_LAYERS);

        // Only consume threads up to the cap
        int layersUsed = Math.max(0, newMax - currentMax);
        if (layersUsed == 0) {
            // Already at cap, don't allow recipe
            return ItemStack.EMPTY;
        }

        result.set(ModDataComponents.MAX_BANNER_LAYERS, newMax);

        return result;
    }

    @Override
    public net.minecraft.core.NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
        net.minecraft.core.NonNullList<ItemStack> remaining = net.minecraft.core.NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);

        // Find banner and thread layer sum
        int currentMax = VANILLA_MAX_BANNER_LAYERS;
        int bannerSlot = -1;
        int[] threadLayers = new int[craftingInput.size()];

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    bannerSlot = i;
                    if (stack.has(ModDataComponents.MAX_BANNER_LAYERS)) {
                        currentMax = stack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
                    }
                } else if (stack.getItem() == ModItems.BANNER_THREAD) {
                    threadLayers[i] = stack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 1);
                }
            }
        }

        int layersToUse = Math.max(0, Math.min(MAX_BANNER_LAYERS - currentMax, java.util.Arrays.stream(threadLayers).sum()));

        // Place unused threads back in their slots, consuming only as many layers as needed
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.BANNER_THREAD) {
                int threadLayer = threadLayers[i];
                if (layersToUse >= threadLayer) {
                    layersToUse -= threadLayer;
                    // fully consumed, do not return
                } else if (layersToUse > 0) {
                    // Partially consumed, return a thread with reduced layers
                    ItemStack leftover = new ItemStack(ModItems.BANNER_THREAD);
                    leftover.set(ModDataComponents.MAX_BANNER_LAYERS, threadLayer - layersToUse);
                    remaining.set(i, leftover);
                    layersToUse = 0;
                } else {
                    // Not consumed at all, return as is
                    remaining.set(i, stack.copyWithCount(1));
                }
            }
        }

        return remaining;
    }
}
