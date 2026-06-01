package dev.svrt.domiirl.mbf.recipe;

import com.mojang.serialization.MapCodec;
import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import dev.svrt.domiirl.mbf.registry.ModItems;
import dev.svrt.domiirl.mbf.registry.ModRecipeSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UseBannerThreadsRecipe extends CustomRecipe {

    public static final int MAX_BANNER_LAYERS = 16, VANILLA_MAX_BANNER_LAYERS = 6;

    public static final MapCodec<UseBannerThreadsRecipe> MAP_CODEC = MapCodec.unit(UseBannerThreadsRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, UseBannerThreadsRecipe> STREAM_CODEC = StreamCodec.unit(new UseBannerThreadsRecipe());
    public static final RecipeSerializer<UseBannerThreadsRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public UseBannerThreadsRecipe() {
        super();
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.USE_BANNER_THREADS;
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
        return !banner.isEmpty() && threadLayerSum > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
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

        int layersUsed = Math.max(0, newMax - currentMax);
        if (layersUsed == 0) {
            return ItemStack.EMPTY;
        }

        result.set(ModDataComponents.MAX_BANNER_LAYERS, newMax);

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
                    if (stack.has(ModDataComponents.MAX_BANNER_LAYERS)) {
                        currentMax = stack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
                    }
                } else if (stack.getItem() == ModItems.BANNER_THREAD) {
                    threadLayers[i] = stack.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 1);
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
                    leftover.set(ModDataComponents.MAX_BANNER_LAYERS, threadLayer - layersToUse);
                    remaining.set(i, leftover);
                    layersToUse = 0;
                } else {
                    remaining.set(i, stack.copyWithCount(1));
                }
            }
        }

        return remaining;
    }
}
